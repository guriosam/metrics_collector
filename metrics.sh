#!/bin/bash

repository=/home/$USER/ant/

while read commit; do
  git --git-dir ${repository}.git reset --hard ${commit}
			
  echo "Criando o projeto ${commit}..."
  ./scitools/bin/linux64/und create -db ${commit}.udb -languages java	
  
  echo "Adicionando os arquivos ao commit ${commit}.."
  ./scitools/bin/linux64/und -db ${commit}.udb add $repository/
  
  echo "Analisando o commit ${commit}..."
  echo "./scitools/bin/linux64/und analyze -db ${commit}.udb"
  ./scitools/bin/linux64/und analyze -db ${commit}.udb

  echo "Adicionando as metricas ${commit}"   
  ./scitools/bin/linux64/und settings -metricmetricsAdd "AvgCyclomatic" "AvgCyclomaticModified" "AvgCyclomaticStrict" "AvgEssential" "AvgLine" "AvgLineBlank" "AvgLineCode" "AvgLineComment" "CountClassBase" "CountClassCoupled" "CountClassDerived" "CountDeclClass" "CountDeclClassMethod" "CountDeclClassVariable" "CountDeclFile" "CountDeclFunction" "CountDeclInstanceMethod" "CountDeclInstanceVariable" "CountDeclMethod" "CountDeclMethodAll" "CountDeclMethodDefault" "CountDeclMethodPrivate" "CountDeclMethodProtected" "CountDeclMethodPublic" "CountInput" "CountLine" "CountLineBlank" "CountLineCode" "CountLineCodeDecl" "CountLineCodeExe" "CountLineComment" "CountOutput" "CountPath" "CountSemicolon" "CountStmt" "CountStmtDecl" "CountStmtExe" "Cyclomatic" "CyclomaticModified" "CyclomaticStrict" "Essential" "MaxCyclomatic" "MaxCyclomaticModified" "MaxCyclomaticStrict" "MaxEssential" "MaxInheritanceTree" "MaxNesting" "PercentLackOfCohesion" "RatioCommentToCode" "SumCyclomatic" "SumCyclomaticModified" "SumCyclomaticStrict" "SumEssential" ${commit}.udb
  
  ./scitools/bin/linux64/und settings -MetricFileNameDisplayMode RelativePath ${commit}.udb
  ./scitools/bin/linux64/und settings -MetricDeclaredInFileDisplayMode RelativePath ${commit}.udb
  ./scitools/bin/linux64/und settings -MetricShowDeclaredInFile on ${commit}.udb
  ./scitools/bin/linux64/und settings -MetricShowFunctionParameterTypes on ${commit}.udb
  
  # Mostra as configurações do projeto
  #./scitools/bin/linux64/und list -metrics settings ${commit}.udb
     
  echo "Calculando as metricas ${commit}"
  ./scitools/bin/linux64/und metrics ${commit}.udb
	
  echo "Removendo projeto ${commit}"
  rm ${commit}.udb

done < commits.txt

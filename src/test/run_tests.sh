#!/usr/bin/env bash
#Usage: sh run_tests.sh

clear
PYTHONPATH_BAK=${PYTHONPATH}
BASE_PROJ=$(pwd | sed "s/\/FIOncoNet\/src\/test//")
export PYTHONPATH="$BASE_PROJ:$PYTHONPATH"
TMP_FILE=cur_stdout.tmp
rm -f ${TMP_FILE}
for f in Test*.py
do
    echo "######################################################" ${f} >> ${TMP_FILE}
    f=$(echo ${f} | cut -d'.' -f1)
    f=$(echo ${f} | sed "s/\//\./g")
    python3 -m ${f} >> ${TMP_FILE} 2>&1
    if grep -q Fail ${TMP_FILE};
    then
        echo "FAILURE DETECTED"
        echo "================"
        break
    fi
    if grep -q Error ${TMP_FILE};
    then
        echo "ERROR DETECTED"
        echo "=============="
        break
    fi
done
cat ${TMP_FILE} | sed '/^$/d'
rm -f ${TMP_FILE}
PYTHONPATH=${PYTHONPATH_BAK}
unset TMP_FILE
unset PYTHONPATH_BAK
unset BASE_PROJ
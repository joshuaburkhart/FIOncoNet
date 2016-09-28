#!/bin/bash

DOCKING_SCHEDULE='/home/burkhart/Downloads/FIsInReactionRASGEFspromoteRASnucleotideechange.xlsx'
INPUT_DIR='/home/burkhart/Software/FIOncoNet/DockingServer/input'
INTERACTOME3D_INPUT_NAME='reaction_5672965'
MACOSX_DIR='__MACOSX'

# Change directory
cd $INPUT_DIR

# Unzip
unzip $INTERACTOME3D_INPUT_NAME.zip

# Remove Unused Directories
rm -rf $MACOSX_DIR
rm -rf $INTERACTOME3D_INPUT_NAME/complete

# Flatten
find $INTERACTOME3D_INPUT_NAME -mindepth 2 -type f -exec mv -t $INTERACTOME3D_INPUT_NAME -f --backup=numbered '{}' +

# Remove empty directories
find $INTERACTOME3D_INPUT_NAME -type d -empty -delete

# Extract
for f in $INTERACTOME3D_INPUT_NAME/*.tar; do tar xf $f -C $INTERACTOME3D_INPUT_NAME; done

# Reflatten, remove old .tar files
find $INTERACTOME3D_INPUT_NAME -mindepth 2 -type f -exec mv -t $INTERACTOME3D_INPUT_NAME -f --backup=numbered '{}' +
rm -rf $INTERACTOME3D_INPUT_NAME/*.tar

# Reremove empty directories
find $INTERACTOME3D_INPUT_NAME -type d -empty -delete

# Create single .dat file
cat $INTERACTOME3D_INPUT_NAME/*.dat > $INTERACTOME3D_INPUT_NAME/model_data.dat

# Keep Only Highest-Coverage Models for Each Uniprot ID, remove old .dat files
cat $INTERACTOME3D_INPUT_NAME/model_data.dat | sort -t$'\t' -k1 | awk -F'\t' '!seen[$1]++' > $INTERACTOME3D_INPUT_NAME/model_data.dat.tmp
rm -rf $INTERACTOME3D_INPUT_NAME/*.dat
mv $INTERACTOME3D_INPUT_NAME/model_data.dat.tmp $INTERACTOME3D_INPUT_NAME/model_data.dat

# Organize pdb files
mkdir $INTERACTOME3D_INPUT_NAME/pdb
mv $INTERACTOME3D_INPUT_NAME/*.pdb $INTERACTOME3D_INPUT_NAME/pdb/

# Copy docking schedule into reaction directory
cp $DOCKING_SCHEDULE $INTERACTOME3D_INPUT_NAME/docking_schedule.xlsx

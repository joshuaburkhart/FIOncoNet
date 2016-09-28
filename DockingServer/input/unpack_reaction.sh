#!/bin/bash

INPUT_DIR='/home/burkhart/Software/FIOncoNet/DockingServer/input'
REACTION='reaction_5672965'
DOCKING_SCHEDULE='/home/burkhart/Downloads/FIsInReactionRASGEFspromoteRASnucleotideechange.xlsx'

# Change directory
cd $INPUT_DIR

# Unzip
unzip $REACTION.zip

# Flatten
find $REACTION -mindepth 2 -type f -exec mv -t $REACTION -f --backup=numbered '{}' +

# Remove empty directories
find $REACTION -type d -empty -delete

# Extract
for f in $REACTION/*.tar; do tar xf $f -C $REACTION; done

# Reflatten
find $REACTION -mindepth 2 -type f -exec mv -t $REACTION -f --backup=numbered '{}' +

# Reremove empty directories
find $REACTION -type d -empty -delete

# Organize pdb files
mkdir $REACTION/pdb
mv $REACTION/*.pdb $REACTION/pdb/

# Copy docking schedule into reaction directory
cp $DOCKING_SCHEDULE $REACTION/docking_schedule.xlsx

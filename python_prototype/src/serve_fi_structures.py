# Webserver providing structures for FIOncoNet

# parse serve_fi_structures.conf

# Load queues from files as synchronized objects

# Thread
# begin processing queues

# Thread
# Open port and service below API calls
#+protein_models_list()
#   list directory contents of protein structure dir in sorted order
#+interaction_models_list()
#   list directory contents of interaction structure dir in sorted order
#+get_protein(id)
#   if id in protein structure dir, send file, else report DNE
#+get_interaction(id)
#   if id in interaction structure dir, send file, else report DNE
#+add_protein(id,priority)
#   if id not in protein queue and not in protein structure dir, add to queue, report success
#   elif id in protein queue with different priority, update priority, report success
#   elif id in protein queue with same priority, report success
#   elif id in protein structure dir, report EXISTS
#+add_interaction(id,priority)
#   if id not in interaction queue and not in interaction structure dir, add to queue, report success
#   elif id in interaction queue with different priority, update priority, report success
#   elif id in interaction queue with same priority, report success
#   elif id in interaction structure dir, report EXISTS


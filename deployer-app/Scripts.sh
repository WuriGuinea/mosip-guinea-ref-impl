#!/bin/sh

sudo su mosipuser
POD_UI=$(kc1 get pod | grep prereg-ui | awk '{print $1}')
kc1 delete pod $POD_UI

exit 0


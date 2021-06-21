@echo off
echo "Arret des Services MDS de capture biometrique"
net stop MosipMVDM
net stop SYNCBYTEMOSIPRDSERVICE
net stop MosipL1MVDM
echo "Demarrage des Services MDS de capture biometrique"
net start SYNCBYTEMOSIPRDSERVICE
timeout 3
net start MosipL1MVDM
timeout 3
net start MosipMVDM
set /p DUMMY=Appuyez sur n'importe quelle touche pour quitter ...
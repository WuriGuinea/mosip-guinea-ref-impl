java -Dspring.profiles.active=prod -Dida.request.captureFinger.deviceId=4200039 -Dida.request.captureIris.deviceId=iris-device-id -Dida.request.captureFace.deviceId=face-device-id -Dmosip.base.url=https://prod.inu.gov.gn -DmispLicenseKey=UmjbDSra8pzOGd5rVtKekTb9D6VdvOQg4Kmw5TzBdw18mbzzME -DpartnerId=748757 -DpartnerApiKey=9418294 -Dida.request.captureFinger.requestedScore=10 -Dida.request.captureFinger.timeout=10000 -jar ./target/authentication-demo-ui-1.0.9.jar
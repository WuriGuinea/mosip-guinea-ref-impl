java -Dspring.profiles.active=local -Dida.request.captureFinger.deviceId=1 -Dida.request.captureIris.deviceId=iris-device-id -Dida.request.captureFace.deviceId=face-device-id -Dmosip.base.url=https://guinea-sandbox.mosip.net -DmispLicenseKey=UmjbDSra8pzOGd5rVtKekTb9D6VdvOQg4Kmw5TzBdw18mbzzME -DpartnerId=748757 -DpartnerApiKey=9418294 -Dida.request.captureFinger.requestedScore=10 -Dida.request.captureFinger.timeout=10000 -jar ./target/authentication-demo-ui-1.0.9.jar
java -Dspring.profiles.active=local -Dida.request.captureFinger.deviceId=1 -Dida.request.captureIris.deviceId=iris-device-id -Dida.request.captureFace.deviceId=face-device-id -Dmosip.base.url=https://sandboxv2.southindia.cloudapp.azure.com -DmispLicenseKey=lqYQppFeepGlHCqlWbJj8EkW2Fs9XkkeVxIEBGtF9X2IKAV9EI -DpartnerId=242447 -DpartnerApiKey=75999518 -Dida.request.captureFinger.requestedScore=10 -Dida.request.captureFinger.timeout=10000 -jar ./target/authentication-demo-ui-1.0.9.jar
package io.mosip.authentication.demo.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.spec.X509EncodedKeySpec;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.stream.Collectors;
import javafx.event.ActionEvent;
import javax.crypto.SecretKey;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.layout.HBox;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.mosip.authentication.demo.dto.AuthRequestDTO;
import io.mosip.authentication.demo.dto.AuthTypeDTO;
import io.mosip.authentication.demo.dto.CryptomanagerRequestDto;
import io.mosip.authentication.demo.dto.EncryptionRequestDto;
import io.mosip.authentication.demo.dto.EncryptionResponseDto;
import io.mosip.authentication.demo.dto.OtpRequestDTO;
import io.mosip.authentication.demo.dto.RequestDTO;
import io.mosip.authentication.demo.helper.CryptoUtility;
import io.mosip.kernel.core.http.RequestWrapper;
import io.mosip.kernel.core.util.CryptoUtil;
import io.mosip.kernel.core.util.DateUtils;
import io.mosip.kernel.core.util.HMACUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import java.util.logging.FileHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * The Class IdaController.
 *
 * @author Sanjay Murali
 * @author condeis
 */
@Component
public class IdaController {

    @Autowired
    private Environment env;

    private static final String ASYMMETRIC_ALGORITHM_NAME = "RSA";

    private static final String SSL = "SSL";

    ObjectMapper mapper = new ObjectMapper();

    @FXML
    ComboBox<String> fingerCount;

    @FXML
    private TextField idValue;

    @FXML
    private TextField idValueVID;
    @FXML
    private CheckBox fingerAuthType;

    @FXML
    private CheckBox otpAuthType;

    @FXML
    private TextField otpValue;

    @FXML
    private AnchorPane otpAnchorPane;

    @FXML
    private AnchorPane bioAnchorPane;

    @FXML
    private TextField responsetextField;

    @FXML
    private ImageView img;

    @FXML
    private Button requestOtp;

    @FXML
    private Button sendAuthRequest;

    private String capture;

    private String previousHash;

    private ObjectMapper objectMapper = new ObjectMapper();

    private SimpleBooleanProperty switchedOn = new SimpleBooleanProperty(true);

    public SimpleBooleanProperty switchOnProperty() {
        return switchedOn;
    }

    @FXML
    private Button tsButton;

    @FXML
    private Label tsLabel;

    private String otpDefaultValue = "Saisir OTP";

    Logger logger = LoggerFactory.getLogger(IdaController.class);


    @FXML
    private void initialize() {

        //    logger.addHandler(fileHandler);

        responsetextField.setText(null);
        ObservableList<String> idTypeChoices = FXCollections.observableArrayList("UIN", "VID", "USERID");
        ObservableList<String> fingerCountChoices = FXCollections.observableArrayList("1", "2", "3", "4", "5", "6", "7",
                "8", "9", "10");
        ObservableList<String> fingerCountChoicesNew = FXCollections.observableArrayList("1- Auriculaire gauche", "2- Annulaire gauche", "3- Majeur gauche",
                "4- Index gauche", "5- Pouce gauche", "6- Pouce droit", "7- Index droit",
                "8- Majeur droit", "9- Annulaire droit", "10- Auriculaire droit");
        fingerCountChoices = fingerCountChoicesNew;

        fingerCount.setItems(fingerCountChoices);
        fingerCount.getSelectionModel().select(0);
        otpAnchorPane.setDisable(true);
        bioAnchorPane.setDisable(true);
        responsetextField.setDisable(true);
        sendAuthRequest.setDisable(true);
        idValue.textProperty().addListener((observable, oldValue, newValue) -> {
            updateSendButton();
        });
        otpValue.textProperty().addListener((observable, oldValue, newValue) -> {
            if (otpValue.isEditable())
                updateSendButton();
        });
        otpValue.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (otpValue.isEditable()  && otpValue.getText().equals(otpDefaultValue))
            {  otpValue.setText("");
                otpValue.setStyle("-fx-text-fill: #020F59;");}

        });
        idValue.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (idValue.isEditable() && idValue.getText().equals("INU")) {
                idValue.setText("");
            }

        });
        idValueVID.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (idValueVID.isEditable())
                idValueVID.setText("");
        });
        switchedOn.addListener((a, b, c) -> {
            if (c) {
                tsLabel.setText("");
                tsLabel.toFront();
                idValueVID.setEditable(false);
                idValue.setEditable(true);
                idValueVID.setStyle("-fx-text-fill: grey;");
                idValue.setStyle("-fx-text-fill: #020F59;");
                idValue.setText("INU");
                idValueVID.setText("VID");
            } else {
                tsLabel.setText("");
                tsHBox.setStyle("-fx-border-color: #020F59;-fx-background-color: white;");
                tsButton.toFront();
                idValueVID.setEditable(true);
                idValue.setEditable(false);
                idValue.setStyle("-fx-text-fill: grey;");
                idValueVID.setStyle("-fx-text-fill: #020F59;");
                idValue.setText("INU");
                idValueVID.setText("VID");
            }
        });
        otpValue.setEditable(false);
        init();
    }

    public void vidDisabled() {
        tsLabel.setText("");
        tsLabel.toFront();
        idValueVID.setEditable(false);
        idValue.setEditable(true);
        idValueVID.setStyle("-fx-text-fill: grey;");
        idValue.setStyle("-fx-text-fill: #020F59;");
        idValue.setText("INU");
        idValueVID.setText("VID");
    }

    @FXML
    private HBox tsHBox;

    @FXML
    private void onFingerPrintAuth() {
        updateBioCapture();
    }

    private void updateBioCapture() {
        capture = null;
        previousHash = null;
        updateBioPane();
        updateSendButton();
    }


    private void setStyle() {
        tsLabel.setAlignment(Pos.CENTER);
    }

    private void init() {
        otpAnchorPane.setStyle( "-fx-border-color: lightgrey;");
        bioAnchorPane.setStyle( "-fx-border-color: lightgrey;");
        idValueVID.setEditable(false);
        idValue.setEditable(true);
        idValueVID.setStyle("-fx-text-color: grey;");
        idValue.setStyle("-fx-text-color: #020F59;");
        tsLabel.setVisible(false);
        tsButton.setOnAction((e) -> {
            switchedOn.set(!switchedOn.get());
        });
        tsLabel.setOnMouseClicked((e) -> {
            switchedOn.set(!switchedOn.get());
        });
        setStyle();
        bindProperties();
        vidDisabled();
    }

    private void bindProperties() {
        tsLabel.prefWidthProperty().bind(tsHBox.widthProperty().divide(2));
        tsLabel.prefHeightProperty().bind(tsHBox.heightProperty());
        tsButton.prefWidthProperty().bind(tsHBox.widthProperty().divide(2));
        tsButton.prefHeightProperty().bind(tsHBox.heightProperty());
    }

    private void updateSendButton() {
        if (idValue.getText() == null ||
                idValue.getText().trim().isEmpty() ||
                idValueVID.getText() == null
                || idValueVID.getText().trim().isEmpty()) {
            sendAuthRequest.setDisable(true);
            return;
        }

        if (otpAuthType.isSelected()) {
            if (otpValue.getText().trim().isEmpty() || otpValue.getText().equals(otpDefaultValue)) {
                sendAuthRequest.setDisable(true);
                return;
            }
        }

        if (isBioAuthType()) {
            if (capture == null) {
                sendAuthRequest.setDisable(true);
                return;
            }
        }
        sendAuthRequest.setDisable(!(isBioAuthType() || otpAuthType.isSelected()));
    }

    private void updateBioPane() {
        if (isBioAuthType()) {
            bioAnchorPane.setDisable(false);
            bioAnchorPane.setStyle( "-fx-border-color: #020F59;");
        } else {
            bioAnchorPane.setDisable(true);
            bioAnchorPane.setStyle( "-fx-border-color: lightgrey;");
        }
        fingerCount.setDisable(!fingerAuthType.isSelected());
    }


    @FXML
    private void onOTPAuth() {
        responsetextField.setText(null);
        otpAnchorPane.setDisable(!otpAnchorPane.isDisable());
        updateSendButton();
        if (otpAuthType.isSelected())
        {
            otpValue.setText(otpDefaultValue);
            otpValue.setStyle("-fx-text-fill: grey;");
            otpAnchorPane.setStyle( "-fx-border-color: #020F59;");

        }
        else
        {
            otpValue.setText("");
            otpAnchorPane.setStyle( "-fx-border-color: lightgrey;");
        }

    }

    @FXML
    private void onIdTypeChange() {
        responsetextField.setText(null);
    }

    @FXML
    private void onSubTypeSelection() {
        responsetextField.setText(null);
    }

    @FXML
    private void onCapture() throws Exception {
        responsetextField.setFont(Font.font("Times New Roman", javafx.scene.text.FontWeight.EXTRA_BOLD, 20));
        previousHash = null;
        List<String> bioCaptures = new ArrayList<>();
        String fingerCapture;
        if (fingerAuthType.isSelected()) {
            fingerCapture = captureFingerprint();
            if (!fingerCapture.contains("\"biometrics\"")) {
                updateSendButton();
                return;
            }
            bioCaptures.add(fingerCapture);
        }


        capture = combineCaptures(bioCaptures);

        updateSendButton();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private String combineCaptures(List<String> bioCaptures) {
        List<String> captures = bioCaptures.stream()
                .filter(obj -> obj != null)
                .filter(str -> str.contains("\"biometrics\""))
                .collect(Collectors.toList());

        if (captures.isEmpty()) {
            return null;
        }

        if (captures.size() == 1) {
            return captures.get(0);
        }

        LinkedHashMap<String, Object> identity = new LinkedHashMap<String, Object>();
        List<Map<String, Object>> biometricsList = captures.stream()
                .map(obj -> {
                    try {
                        return objectMapper.readValue(obj, Map.class);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .map(map -> map.get("biometrics"))
                .filter(obj -> obj instanceof List)
                .map(obj -> (List<Map>) obj)
                .flatMap(list -> list.stream())
                .filter(obj -> obj instanceof Map)
                .map(obj -> (Map<String, Object>) obj)
                .collect(Collectors.toList());
        identity.put("biometrics", biometricsList);

        try {
            return objectMapper.writeValueAsString(identity);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String captureFingerprint() throws Exception {
        responsetextField.setText("Capture de l'empreinte...");
        responsetextField.setStyle("-fx-text-fill: black; -fx-font-size: 20px; -fx-font-weight: bold");
        String requestBody = env.getProperty("ida.request.captureRequest.template");
        requestBody = requestBody.replace("$timeout", env.getProperty("ida.request.captureFinger.timeout"))
                .replace("$count", getFingerCount()).
                        replace("$deviceId", env.getProperty("ida.request.captureFinger.deviceId")).
                        replace("$domainUri", env.getProperty("ida.request.captureFinger.domainUri")).
                        replace("$deviceSubId", getFingerDeviceSubId()).
                        replace("$captureTime", getCaptureTime()).
                        replace("$previousHash", getPreviousHash()).
                        replace("$requestedScore", env.getProperty("ida.request.captureFinger.requestedScore")).
                        replace("$type", env.getProperty("ida.request.captureFinger.type")).
                        replace("$bioSubType", getBioSubType(getFingerCount(), env.getProperty("ida.request.captureFinger.bioSubType"))).
                        replace("$name", env.getProperty("ida.request.captureFinger.name")).
                        replace("$value", env.getProperty("ida.request.captureFinger.value"));

        return capturebiometrics(requestBody);
    }


    private String getFingerDeviceSubId() {
        return "0";
    }


    private String getPreviousHash() {
        return previousHash == null ? "" : previousHash;
    }

    private String getFingerCount() {
        return fingerCount.getValue() == null ? String.valueOf(1) : fingerCount.getValue();
    }

    private String getBioSubType(String count, String bioValue) {
        if (count.equalsIgnoreCase("1")) {
            return "\"" + bioValue + "\"";
        }
        String finalStr = "\"" + bioValue + "\"";
        for (int i = 2; i <= Integer.parseInt(count); i++) {
            finalStr = finalStr + "," + "\"" + bioValue + "\"";
        }

        return finalStr;
    }


    private String getCaptureTime() {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'"); // Quoted "Z" to indicate UTC, no timezone offset
        df.setTimeZone(tz);
        String nowAsISO = df.format(new Date());
        return nowAsISO;
    }

    @SuppressWarnings("rawtypes")
    private String capturebiometrics(String requestBody) throws Exception {
        logger.info("Capture request:\n" + requestBody);
        CloseableHttpClient client = HttpClients.createDefault();
        StringEntity requestEntity = new StringEntity(requestBody, ContentType.APPLICATION_JSON);
        HttpUriRequest request = RequestBuilder.create("CAPTURE").setUri(env.getProperty("ida.captureRequest.uri"))
                .setEntity(requestEntity).build();
        CloseableHttpResponse response;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            response = client.execute(request);

            InputStream inputStram = response.getEntity().getContent();
            BufferedReader bR = new BufferedReader(new InputStreamReader(inputStram));
            String line = null;
            while ((line = bR.readLine()) != null) {
                stringBuilder.append(line);
            }
            bR.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String result = stringBuilder.toString();
        String error = ((Map) mapper.readValue(result, Map.class).get("error")).get("errorCode").toString();

        if (error.equals("0")) {
            responsetextField.setText("Capture réussie");
            responsetextField.setStyle("-fx-text-fill: green; -fx-font-size: 20px; -fx-font-weight: bold");
            ObjectMapper objectMapper = new ObjectMapper();
            List dataList = (List) objectMapper.readValue(result.getBytes(), Map.class).get("biometrics");
            for (int i = 0; i < dataList.size(); i++) {
                Map b = (Map) dataList.get(i);
                String dataJws = (String) b.get("data");
                Map dataMap = objectMapper.readValue(CryptoUtil.decodeBase64(dataJws.split("\\.")[1]), Map.class);
                logger.info((i + 1) + " Bio-type: " + dataMap.get("bioType") + " Bio-sub-type: " + dataMap.get("bioSubType"));
                previousHash = (String) b.get("hash");
            }
        } else {
            responsetextField.setText("Erreur de capture");
            responsetextField.setStyle("-fx-text-fill: red; -fx-font-size: 20px; -fx-font-weight: bold");
        }
        logger.info(result);

        return result;
    }

    Stage stage;

    @FXML
    private void closeAction(ActionEvent event) {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
    }


    @FXML
    private void minimizeAction(ActionEvent event) {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    private void maximizeAction(ActionEvent event) {
        //   stage=(Stage)((Button) event.getSource()).getScene().getWindow();
        //git   stage.setMaximized(true);
    }

    @SuppressWarnings("rawtypes")
    @FXML
    private void onRequestOtp() {

        otpValue.setStyle("-fx-text-fill: grey;");

        otpValue.setText(otpDefaultValue);

        String type = "UIN";
        responsetextField.setText(null);
        OtpRequestDTO otpRequestDTO = new OtpRequestDTO();
        otpRequestDTO.setId("mosip.identity.otp");
        String valueToCheck = idValue.getText();
        if (valueToCheck.contains("UIN")) {
            valueToCheck = idValueVID.getText();
            type = "VID";
        }
        otpRequestDTO.setIndividualId(valueToCheck);
        otpRequestDTO.setIndividualIdType(type);
        otpRequestDTO.setOtpChannel(Collections.singletonList("email"));
        otpRequestDTO.setRequestTime(getUTCCurrentDateTimeISOString());
        otpRequestDTO.setTransactionID(getTransactionID());
        otpRequestDTO.setVersion("1.0");

        try {
            RestTemplate restTemplate = createTemplate();
            HttpEntity<OtpRequestDTO> httpEntity = new HttpEntity<>(otpRequestDTO);
            ResponseEntity<Map> response = restTemplate.exchange(
                    env.getProperty("ida.otp.url"),
                    HttpMethod.POST, httpEntity, Map.class);
            System.err.println(response);
            logger.error(""+response);

            if (response.getStatusCode().is2xxSuccessful()) {
                List errors = ((List) response.getBody().get("errors"));
                boolean status = errors == null || errors.isEmpty();
                String responseText = status ? "Succès de la requête OTP" : "Echec de la requête OTP";
                if (status) {
                    responsetextField.setStyle("-fx-text-fill: green; -fx-font-size: 20px; -fx-font-weight: bold");
                    otpValue.setEditable(true);
                    otpValue.setText(otpDefaultValue);


                } else {
                    responsetextField.setStyle("-fx-text-fill: red; -fx-font-size: 20px; -fx-font-weight: bold");
                }
                responsetextField.setText(responseText);
            } else {
                responsetextField.setText("Erreur d'envoi de la requête OTP");
                responsetextField.setStyle("-fx-text-fill: red; -fx-font-size: 13px; -fx-font-weight: bold");
            }

        } catch (KeyManagementException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @FXML
    private void onSendAuthRequest() throws Exception {
        responsetextField.setText("null");
        responsetextField.setStyle("-fx-text-fill: black; -fx-font-size: 20px; -fx-font-weight: bold");
        responsetextField.setText("Preparation de la requête d'authentification");
        AuthRequestDTO authRequestDTO = new AuthRequestDTO();
        // Set Auth Type
        AuthTypeDTO authTypeDTO = new AuthTypeDTO();
        authTypeDTO.setBio(isBioAuthType());
        authTypeDTO.setOtp(isOtpAuthType());
        authRequestDTO.setRequestedAuth(authTypeDTO);
        // set Individual Id
        authRequestDTO.setIndividualId(idValue.getText());
        // Set Individual Id type
        authRequestDTO.setIndividualIdType("UIN");

        RequestDTO requestDTO = new RequestDTO();
        requestDTO.setTimestamp(getUTCCurrentDateTimeISOString());

        if (isOtpAuthType()) {
            requestDTO.setOtp(otpValue.getText());
        }

        Map<String, Object> identityBlock = mapper.convertValue(requestDTO, Map.class);
        if (isBioAuthType()) {
            identityBlock.put("biometrics", mapper.readValue(capture, Map.class).get("biometrics"));
        }
        responsetextField.setText("Requête d'authentification...");
        logger.info("******* Request before encryption ************ \n\n");
        EncryptionRequestDto encryptionRequestDto = new EncryptionRequestDto();
        encryptionRequestDto.setIdentityRequest(identityBlock);
        EncryptionResponseDto kernelEncrypt = null;
        try {
            responsetextField.setText("Requête d'authentification...");
            kernelEncrypt = kernelEncrypt(encryptionRequestDto, false);
        } catch (Exception e) {
            logger.info ("Error"+e);
            responsetextField.setText(" Erreur d'encryption de la requête d'authentification");
            return;
        }

        responsetextField.setText("Authentification en cours...");
        // Set request block
        authRequestDTO.setRequest(requestDTO);

        authRequestDTO.setTransactionID(getTransactionID());
        authRequestDTO.setRequestTime(getUTCCurrentDateTimeISOString());
        authRequestDTO.setConsentObtained(true);
        authRequestDTO.setId(getAuthRequestId());
        authRequestDTO.setVersion("1.0");

        Map<String, Object> authRequestMap = mapper.convertValue(authRequestDTO, Map.class);
        authRequestMap.replace("request", kernelEncrypt.getEncryptedIdentity());
        authRequestMap.replace("requestSessionKey", kernelEncrypt.getEncryptedSessionKey());
        authRequestMap.replace("requestHMAC", kernelEncrypt.getRequestHMAC());
        RestTemplate restTemplate = createTemplate();
        HttpEntity<Map> httpEntity = new HttpEntity<>(authRequestMap);
        String url = getUrl();
        logger.info("Auth URL: " + url);
        logger.info("Auth Request : \n" + new ObjectMapper().writeValueAsString(authRequestMap));

        try {
            ResponseEntity<Map> authResponse = restTemplate.exchange(url,
                    HttpMethod.POST, httpEntity, Map.class);
            if (authResponse.getStatusCode().is2xxSuccessful()) {
                boolean status = (boolean) ((Map<String, Object>) authResponse.getBody().get("response")).get("authStatus");
                String response = status ? "Authentification réussie" : "Echec d'authentification";
                if (status) {
                    responsetextField.setStyle("-fx-text-fill: green; -fx-font-size: 15px; -fx-font-weight: bold");
                } else {
                    responsetextField.setStyle("-fx-text-fill: red; -fx-font-size: 15px; -fx-font-weight: bold");
                }
                String content=responsetextField.getText();
                responsetextField.setText(response);
            } else {
                responsetextField.setText("Echec de le requête d'authentification avec des erreurs");
                responsetextField.setStyle("-fx-text-fill: red; -fx-font-size: 15px; -fx-font-weight: bold");
            }
            logger.info("Auth Response : \n" + new ObjectMapper().writeValueAsString(authResponse));
            logger.info(""+authResponse.getBody());
        } catch (Exception e) {
            // e.printStackTrace();
            logger.error("Error:"+e);
            responsetextField.setText("Echec d'authentification avec erreur");
            responsetextField.setStyle("-fx-text-fill: red; -fx-font-size: 20px; -fx-font-weight: bold");
        }
    }

    private String getAuthRequestId() {
        return env.getProperty("authRequestId", "mosip.identity.auth");
    }

    private boolean isOtpAuthType() {
        return otpAuthType.isSelected();
    }

    private String getUrl() {
        return env.getProperty("ida.auth.url");
    }

    private boolean isBioAuthType() {
        return fingerAuthType.isSelected();//|| irisAuthType.isSelected() || faceAuthType.isSelected();
    }

    private EncryptionResponseDto kernelEncrypt(EncryptionRequestDto encryptionRequestDto, boolean isInternal)
            throws Exception {
        EncryptionResponseDto encryptionResponseDto = new EncryptionResponseDto();
        String identityBlock = mapper.writeValueAsString(encryptionRequestDto.getIdentityRequest());

        SecretKey secretKey = cryptoUtil.genSecKey();

        byte[] encryptedIdentityBlock = cryptoUtil.symmetricEncrypt(identityBlock.getBytes(), secretKey);
        encryptionResponseDto.setEncryptedIdentity(Base64.encodeBase64URLSafeString(encryptedIdentityBlock));
        String publicKeyStr = getPublicKey(identityBlock, isInternal);
        PublicKey publicKey = KeyFactory.getInstance(ASYMMETRIC_ALGORITHM_NAME)
                .generatePublic(new X509EncodedKeySpec(CryptoUtil.decodeBase64(publicKeyStr)));
        byte[] encryptedSessionKeyByte = cryptoUtil.asymmetricEncrypt((secretKey.getEncoded()), publicKey);
        encryptionResponseDto.setEncryptedSessionKey(Base64.encodeBase64URLSafeString(encryptedSessionKeyByte));
        byte[] byteArr = cryptoUtil.symmetricEncrypt(
                HMACUtils.digestAsPlainText(HMACUtils.generateHash(identityBlock.getBytes())).getBytes(), secretKey);
        encryptionResponseDto.setRequestHMAC(Base64.encodeBase64URLSafeString(byteArr));
        return encryptionResponseDto;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public String getPublicKey(String data, boolean isInternal)
            throws KeyManagementException, RestClientException, NoSuchAlgorithmException {
        RestTemplate restTemplate = createTemplate();

        CryptomanagerRequestDto request = new CryptomanagerRequestDto();
        request.setApplicationId("IDA");
        request.setData(Base64.encodeBase64URLSafeString(data.getBytes(StandardCharsets.UTF_8)));
        String publicKeyId = env.getProperty("ida.reference.id");
        request.setReferenceId(publicKeyId);
        String utcTime = getUTCCurrentDateTimeISOString();
        request.setTimeStamp(utcTime);
        Map<String, String> uriParams = new HashMap<>();
        uriParams.put("appId", "IDA");
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromUriString(
                        env.getProperty("ida.publickey.url") + "/IDA")
                .queryParam("timeStamp", getUTCCurrentDateTimeISOString())
                .queryParam("referenceId", publicKeyId);
        ResponseEntity<Map> response = restTemplate.exchange(builder.build(uriParams), HttpMethod.GET, null, Map.class);
        return (String) ((Map<String, Object>) response.getBody().get("response")).get("publicKey");
    }

    private RestTemplate createTemplate() throws KeyManagementException, NoSuchAlgorithmException {
        turnOffSslChecking();
        RestTemplate restTemplate = new RestTemplate();
        ClientHttpRequestInterceptor interceptor = new ClientHttpRequestInterceptor() {

            @Override
            public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
                    throws IOException {
                String authToken = generateAuthToken();
                if (authToken != null && !authToken.isEmpty()) {
                    request.getHeaders().set("Cookie", "Authorization=" + authToken);
                }
                return execution.execute(request, body);
            }
        };

        restTemplate.setInterceptors(Collections.singletonList(interceptor));
        return restTemplate;
    }

    private String generateAuthToken() {
        ObjectNode requestBody = mapper.createObjectNode();
        requestBody.put("clientId", env.getProperty("clientId"));
        requestBody.put("secretKey", env.getProperty("secretKey"));
        requestBody.put("appId", env.getProperty("appId"));
        RequestWrapper<ObjectNode> request = new RequestWrapper<>();
        request.setRequesttime(DateUtils.getUTCCurrentDateTime());
        request.setRequest(requestBody);
        ClientResponse response = WebClient
                .create(env.getProperty("ida.authmanager.url"))
                .post().syncBody(request).exchange().block();
        List<ResponseCookie> list = response.cookies().get("Authorization");
        if (list != null && !list.isEmpty()) {
            ResponseCookie responseCookie = list.get(0);
            return responseCookie.getValue();
        }
        return "";
    }

    @SuppressWarnings("unused")
    private HttpEntity<CryptomanagerRequestDto> getHeaders(CryptomanagerRequestDto req) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        return new HttpEntity<CryptomanagerRequestDto>(req, headers);
    }


    private static final TrustManager[] UNQUESTIONING_TRUST_MANAGER = new TrustManager[]{new X509TrustManager() {
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public void checkClientTrusted(java.security.cert.X509Certificate[] arg0, String arg1)
                throws CertificateException {
        }

        public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String arg1)
                throws CertificateException {
        }
    }};

    @Autowired
    private CryptoUtility cryptoUtil;

    public static void turnOffSslChecking() throws KeyManagementException, java.security.NoSuchAlgorithmException {
        // Install the all-trusting trust manager
        final SSLContext sc = SSLContext.getInstance(SSL);
        sc.init(null, UNQUESTIONING_TRUST_MANAGER, null);
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    }

    public static String getUTCCurrentDateTimeISOString() {
        return DateUtils.formatToISOString(DateUtils.getUTCCurrentDateTime());
    }

    /**
     * Gets the transaction ID.
     *
     * @return the transaction ID
     */
    public static String getTransactionID() {
        return "1234567890";
    }

    @FXML
    private void onReset() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);


        //   alert.getHeader().setStyle("-fx-text-fill:#020F59;-fx-font-size:28.0px;");
        alert.setContentText("Etes vous sur de vouloir annuler?");
        ButtonType okButton = new ButtonType("Oui", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("Non", ButtonBar.ButtonData.NO);
        alert.getButtonTypes().setAll(okButton, noButton);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.initModality(Modality.WINDOW_MODAL);
        alert.getDialogPane().getStylesheets().add("alert.css");

        alert.showAndWait().ifPresent(type -> {
            if (type.getButtonData().equals(ButtonType.YES.getButtonData())) {
                reset();
            }
        });
    }

    private void reset() {
        fingerCount.getSelectionModel().select(0);
        //	irisCount.getSelectionModel().select(0);
        idValue.setText("");
        fingerAuthType.setSelected(false);
        //	irisAuthType.setSelected(false);
        //	faceAuthType.setSelected(false);
        otpAuthType.setSelected(false);
        //idTypebox.setValue("UIN");
        otpValue.setText("");
        otpAnchorPane.setDisable(true);
        bioAnchorPane.setDisable(true);
        responsetextField.setText("");
        sendAuthRequest.setDisable(false);
        capture = null;
        previousHash = null;
        updateBioPane();
        updateSendButton();
        init();
    }

    @FXML
    private void onOtpValueUpdate() {
        updateSendButton();
    }
}

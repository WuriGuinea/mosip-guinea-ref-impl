import {HttpClient, HttpHeaders} from "@angular/common/http";
import { Injectable } from "@angular/core";
import {Observable} from "rxjs";


@Injectable({providedIn: 'root'})
export class ContactUsService {

    BASE_URL = "https://guinea-sandbox.mosip.net/pre-registration-contactus";

    constructor(private httpClient: HttpClient) { }

    sendForm(formData: any) {
        const url = this.BASE_URL + "/contact-us";
        const data = {
            "name" : formData.name,
            "email" : formData.email,
            "reason" :  formData.reason.toLocaleLowerCase() == 'autre' ? formData.otherReason : formData.reason,
            "sign" : "",
            "message" : `<h2> Objet: Accusé de réception - RE[${formData.reason}]</h2> <h6> ${formData.name}, </h6> <p> Nous avons reçu votre message et un membre de notre équipe vous contactera dans les plus bref délais. Ceci est un réponse automatique. </p><p> Votre message: <br><br>&nbsp; &nbsp; À: &nbsp; &nbsp; WURI GUINES <br>&nbsp; &nbsp; Objet: ${formData.reason} <br>&nbsp; &nbsp; Envoyé: contact@wuriguinee.com <br>&nbsp; &nbsp; Message: ${formData.message} <br></p><p> Cordialement, <br>L'Équipe WURI Guinée </p>`
        };
        console.log(data);
        return this.httpClient.post(url, data);
    }

    verifyMyCaptcha(token: string): Observable<any> {
        const headers = new HttpHeaders({
            'Content-Type':'application/json; charset=utf-8',
            'token': token
        });
        const url = this.BASE_URL + "/contact-us/captcha";


        return this.httpClient.post(url, {}, {
            headers: headers
        });
    }
}

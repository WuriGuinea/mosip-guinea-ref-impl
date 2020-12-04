import {HttpClient, HttpHeaders} from "@angular/common/http";
import { Injectable } from "@angular/core";
import {Observable} from "rxjs";


@Injectable({
    providedIn: 'root'
  })
export class ContactUsService {

    BASE_URL = "https://guinea-sandbox.mosip.net/pre-registration-contactus";

    constructor(private httpClient: HttpClient) { }

    sendForm(formData: object) {
        const url = this.BASE_URL + "/contact-us"
        return this.httpClient.post(url, formData);
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

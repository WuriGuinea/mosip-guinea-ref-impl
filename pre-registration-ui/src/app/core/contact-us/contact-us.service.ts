import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";


@Injectable({
    providedIn: 'root'
  })
export class ContactUsService {

    BASE_URL = "http://localhost:8081";

    constructor(private httpClient: HttpClient) { }

    sendForm(formData: object) {
        const url = this.BASE_URL + "/contact-us"
        return this.httpClient.post(url, formData);
    }
}
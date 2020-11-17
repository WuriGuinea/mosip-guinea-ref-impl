import {HttpClient, HttpHeaders} from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MatButtonToggleChange } from '@angular/material';
import { ContactUs, ContactUsFormControlModal } from './contact-us';

@Component({
  selector: 'app-contact-us',
  templateUrl: './contact-us.component.html',
  styleUrls: ['./contact-us.component.css']
})
export class ContactUsComponent implements OnInit {

  BASE_URL = "http://localhost:8081";
  reasons: object[] = ContactUs.Reasons;
  userForm: FormGroup;
  displayOtherReason: boolean = false;
  formControlValues: ContactUsFormControlModal;
  formControlNames: ContactUsFormControlModal = {
    fullName: 'fullName',
    email: 'email',
    reason: 'reason',
    otherReason: 'otherReason',
    message: 'message',
  };

  constructor(private httpClient: HttpClient) { }

  ngOnInit() {
    this.setFormControlValues();
    this.userForm = new FormGroup({
      [this.formControlNames.fullName]: new FormControl(this.formControlValues.fullName.trim(), [
        Validators.required,
        this.noWhitespaceValidator
      ]),
      [this.formControlNames.email]: new FormControl(this.formControlValues.email, [
        Validators.pattern(/^[\w-\+]+(\.[\w]+)*@[\w-]+(\.[\w]+)*(\.[a-zA-Z]{2,})$/)
      ]),
      [this.formControlNames.reason]: new FormControl(this.formControlValues.reason.trim(), [
        Validators.required,
        this.noWhitespaceValidator
      ]),
      [this.formControlNames.otherReason]: new FormControl(this.formControlValues.otherReason.trim(), [
        Validators.required,
        this.noWhitespaceValidator
      ]),
      [this.formControlNames.message]: new FormControl(this.formControlValues.message.trim(), [
        Validators.required,
        this.noWhitespaceValidator
      ])
    });
  }

  setFormControlValues() {
    this.formControlValues = {
      fullName: '',
      email: '',
      message: '',
      otherReason: '',
      reason: ''
    };
  }

  onSubmit() {

    console.log(this.userForm.getRawValue());
    this.sendForm(this.userForm.getRawValue()).subscribe(
        response => {
          const r = response;
          console.log(r);
        },
        error => {
          const err = error;
          console.log(err);
        });


    this.markFormGroupTouched(this.userForm);
    if (this.userForm.valid) {
      const request = this.userForm.value;
      this.sendForm(request).subscribe(
        response => {
          const r = response;
          console.log(r);
        },
        error => {
          const err = error;
          console.log(err);
        });
    }
  }

  onReasonChange(entity: any, event?: MatButtonToggleChange) {
    this.displayOtherReason = "other" === event.value;
  }

  private noWhitespaceValidator(control: FormControl) {
    const isWhitespace = (control.value || '').trim().length === 0;
    const isValid = !isWhitespace;
    return isValid ? null : { whitespace: true };
  }

  private markFormGroupTouched(formGroup: FormGroup) {
    (<any>Object).values(formGroup.controls).forEach(control => {
      control.markAsTouched();
      if (control.controls) {
        this.markFormGroupTouched(control);
      }
    });
  }

  private sendForm(data: object) {
    const headers= new HttpHeaders({
      'Content-Type': 'application/json',
      'Accept': 'application/json',
      'Access-Control-Allow-Origin': 'http://localhost:8081/'
    });

    const url = this.BASE_URL + "/contact-us";
    return this.httpClient.post(url, data, { 'headers': headers });
  }
}


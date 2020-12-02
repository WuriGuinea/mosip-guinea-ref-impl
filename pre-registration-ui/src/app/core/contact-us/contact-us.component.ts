import { HttpClient } from '@angular/common/http';
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

  BASE_URL = "https://guinea-sandbox.mosip.net/pre-registration-contactus";
  reasons: object[] = ContactUs.Reasons;
  userForm: FormGroup;
  displayOtherReason: boolean = false;
  formControlValues: ContactUsFormControlModal;
  formControlNames: ContactUsFormControlModal = {
    name: 'name',
    email: 'email',
    reason: 'reason',
    otherReason: 'otherReason',
    objet: 'object',
    message: 'message',
  };

  constructor(private httpClient: HttpClient) { }

  ngOnInit() {
    this.setFormControlValues();
    this.userForm = new FormGroup({
      [this.formControlNames.name]: new FormControl(this.formControlValues.name.trim(), [
        Validators.required 
      ]),
      [this.formControlNames.email]: new FormControl(this.formControlValues.email, [
        Validators.pattern(/^[\w-\+]+(\.[\w]+)*@[\w-]+(\.[\w]+)*(\.[a-zA-Z]{2,})$/),
          Validators.required
      ]),
      [this.formControlNames.reason]: new FormControl(this.formControlValues.reason.trim(), [
        Validators.required 
      ]),
      [this.formControlNames.otherReason]: new FormControl(this.formControlValues.otherReason.trim(), [
        Validators.required 
      ]),
      [this.formControlNames.message]: new FormControl(this.formControlValues.message.trim(), [
        Validators.required 
      ])
    });
  }

  setFormControlValues() {
    this.formControlValues = {
      name: '',
      email: '',
      message: '',
      otherReason: '',
      objet: '',
      reason: ''
    };
  }

  onSubmit() {
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
  
  private markFormGroupTouched(formGroup: FormGroup) {
    (<any>Object).values(formGroup.controls).forEach(control => {
      control.markAsTouched();
      if (control.controls) {
        this.markFormGroupTouched(control);
      }
    });
  }

  private sendForm(data: object) {
    const url = this.BASE_URL + "/contact-us";
    return this.httpClient.post(url, data);
  }

}

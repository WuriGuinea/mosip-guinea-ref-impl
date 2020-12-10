import { HttpClient } from '@angular/common/http';
import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MatButtonToggleChange } from '@angular/material';
import {MatDialog} from "@angular/material/dialog";
import {ConfirmationDialogComponent} from "./confirmation-dialog/confirmation-dialog.component";
import { ContactUs, ContactUsFormControlModal } from './contact-us';
import {ContactUsService} from "./contact-us.service";
declare var grecaptcha: any;



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
    captcha: ''
  };

  invalidLogin: boolean = false;
  captchaError = '';
  loginResponse: string;
  captchaValidated = false;

  constructor(
      private httpClient: HttpClient,
      private contactUsService: ContactUsService,
      public dialog: MatDialog
  ) { }

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
      ]),
      [this.formControlNames.captcha]: new FormControl(this.formControlValues.captcha, [])
    });
  }

  openDialog(): void {
    const dialogRef = this.dialog.open(ConfirmationDialogComponent, {
      width: '550px',
      disableClose: true,
      data: {}
    });

    dialogRef.afterClosed().subscribe(result => {
      this.userForm.reset();
    });
  }

  setFormControlValues() {
    this.formControlValues = {
      name: '',
      email: '',
      message: '',
      otherReason: '',
      objet: '',
      reason: '',
      captcha: ''
    };
  }

  onSubmit() {
    this.openDialog();
    const response = grecaptcha.getResponse();
    if (response.length === 0) {
      this.captchaError = '';
      return;
    }

    this.markFormGroupTouched(this.userForm);
    if (this.userForm.valid) {
      const request = this.userForm.value;
      this.sendForm(request).subscribe(
          response => {
            const r = response;
            console.log(r);

            this.openDialog();
          },
          error => {
            this.invalidLogin = true;
            this.loginResponse = response.message;
            const err = error;
            console.log(err);

            //this.openDialog();
          });
      grecaptcha.reset();
    }
  }

  onReasonChange(entity: any, event?: MatButtonToggleChange) {
    console.log(event.value);
    this.displayOtherReason = "AUTRE" === event.value;
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

  resolved(token: any) {
    console.log(token);
    this.contactUsService.verifyMyCaptcha(token).subscribe(response => {
      this.captchaValidated = true;
      console.log(response);
    }, error => {
      console.log(error);
      this.captchaError = 'invalid';
    });
  }
}

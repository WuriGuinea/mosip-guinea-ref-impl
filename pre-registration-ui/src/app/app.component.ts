import {HttpClient, HttpHeaders} from "@angular/common/http";
import { Component, OnInit, OnDestroy } from '@angular/core';
import { HostListener } from '@angular/core';
import { Event as NavigationEvent, Router } from '@angular/router';
import { filter } from 'rxjs/operators';
import { NavigationStart } from '@angular/router';

import { AutoLogoutService } from 'src/app/core/services/auto-logout.service';
import { ConfigService } from './core/services/config.service';
import { Subscription } from 'rxjs';
import {AppConfigService} from "./app-config.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit, OnDestroy {
  title = 'pre-registration';
  message: object;
  subscriptions: Subscription[] = [];

  constructor(
    private autoLogout: AutoLogoutService,
    private router: Router,
    private configService: ConfigService,
    private appConfigService: AppConfigService,
    private http: HttpClient
  ) {
    this.router.navigate(['/']);

    const data = {
      "name" : "Mamadou Yaya DIALLO",
      "email" : "yayamombeya090@gmail.com",
      "reason" :  "DEMANDE INFOR",
      "otherReason" : null,
      "message" : "Need some information, please call me on 622315214"
    };

    const headers= new HttpHeaders({
      'Content-Type': 'application/json',
      'Accept': 'application/json',
      'Access-Control-Allow-Origin': '*'
    });

    const url = "https://guinea-sandbox.mosip.net/pre-registration-contactus/contact-us";
    // @ts-ignore
    this.http.post(url, data, headers).subscribe(r => {
      console.log(r);
    }, error => {
      console.log(error);
    });
  }

  ngOnInit() {
    this.subscriptions.push(this.autoLogout.currentMessageAutoLogout.subscribe(() => {}));
    this.autoLogout.changeMessage({ timerFired: false });
    this.routerType();
  }

  routerType() {
    this.subscriptions.push(
      this.router.events
        .pipe(filter((event: NavigationEvent) => event instanceof NavigationStart))
        .subscribe((event: NavigationStart) => {
          if (event.restoredState) {
            this.configService.navigationType = 'popstate';
            this.preventBack();
          }
        })
    );
  }

  preventBack() {
    window.history.forward();
    window.onunload = function() {
      null;
    };
  }

  @HostListener('mouseover')
  @HostListener('document:mousemove', ['$event'])
  @HostListener('keypress')
  @HostListener('click')
  @HostListener('document:keypress', ['$event'])
  onMouseClick() {
    this.autoLogout.setisActive(true);
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(subscription => subscription.unsubscribe());
  }
}

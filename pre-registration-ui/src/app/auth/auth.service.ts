import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { UserIdleService } from 'angular-user-idle';
import { DataStorageService } from '../core/services/data-storage.service';
import { BehaviorSubject } from 'rxjs';
import * as appConstants from "../app.constants";
import {AppConfigService} from "../app-config.service";
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  myProp = new BehaviorSubject<boolean>(false);
  constructor(
    private router: Router,
    private httpClient: HttpClient,
    private appConfigService: AppConfigService,
    private dataStorageService: DataStorageService,
    private userIdle: UserIdleService
  ) {}

  myProp$ = this.myProp.asObservable();
  token: string;
  BASE_URL = this.appConfigService.getConfig()['BASE_URL'];
  PRE_REG_URL = this.appConfigService.getConfig()['PRE_REG_URL'];

  getLogin(){
    return new Promise(resolve => {
      const url = this.BASE_URL + appConstants.APPEND_URL.gender;
      this.httpClient.get(url, {observe: 'response'}).subscribe(
        response => {
          console.log("GetLogin: " + response.status);
          // localStorage.setItem('loggedIn', 'true');
          resolve(true);
        },
        error => {
          console.log(error);
          resolve(false);
        }
      );
    });
  }

  setToken() {
    // this.token = 'settingToken';
    this.myProp.next(true);
  }

  removeToken() {
    this.token = null;
    this.myProp.next(false);
  }

  isAuthenticated() {
    return this.token != null;
  }

  onLogout() {
    // localStorage.setItem('loggedIn', 'false');
    // localStorage.setItem('loggedOut', 'true');
    this.removeToken();
    this.dataStorageService.onLogout().subscribe();
    this.router.navigate(['/']);
    this.userIdle.stopWatching();
  }
}

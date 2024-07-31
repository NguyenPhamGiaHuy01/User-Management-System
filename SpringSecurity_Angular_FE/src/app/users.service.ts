import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';

import { Observable } from 'rxjs';
@Injectable({
  providedIn: 'root'
})
export class UsersService {

  private BASE_URL = "http://localhost:8080";
  constructor(private http: HttpClient) { }

  login(email: string, password: string):Observable<any>{
    const url = `${this.BASE_URL}/auth/login`;
    try {
      const response = this.http.post(url,{email,password});
      return response;
    } catch (error) {
        throw error;
    }
  }

  register(signupRequest: any, token: string): Observable<any>{
    const url = `${this.BASE_URL}/auth/register`;
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    })
    try {
      const response = this.http.post(url,signupRequest,{headers});
      return response;
    } catch (error) {
        throw error;
    }
  }
  async getAllUsers(token:string):Promise<any>{
    const url = `${this.BASE_URL}/admin/get-all-users`;
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    })
    try{
      const response =  this.http.get<any>(url, {headers}).toPromise()
      return response;
    }catch(error){
      throw error;
    }
  }

  getYourProfile(token: string):Observable<any>{
    const url = `${this.BASE_URL}/adminuser/get-profile`;
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    })
    try {
      const response = this.http.get(url,{headers});
      return response;
    } catch (error) {
        throw error;
    }
  }

  async getUsersById(userId: string, token:string):Promise<any>{
    const url = `${this.BASE_URL}/admin/get-users/${userId}`;
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    })
    try{
      const response =  this.http.get<any>(url, {headers}).toPromise()
      return response;
    }catch(error){
      throw error;
    }
  }


  deleteUser(userId: string, token: string):Observable<any>{
    const url = `${this.BASE_URL}/admin/delete/${userId}`;
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    })
    try {
      const response = this.http.delete(url,{headers});
      return response;
    } catch (error) {
        throw error;
    }
  }

  updateUser(userId: string, userData: any, token: string):Observable<any>{
    const url = `${this.BASE_URL}/admin/update/${userId}`;
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    })
    try {
      const response = this.http.put(url,userData,{headers});
      return response;
    } catch (error) {
        throw error;
    }
  }


  /**AUTHENCATION METHODS */
  logOut():void{
    if (typeof localStorage !== 'undefined') {
      localStorage.removeItem('token')
      localStorage.removeItem('role')
    } else {
      
    }
  }

  isAuthenticated():boolean{
    if (typeof localStorage !== 'undefined') {
      const token = localStorage.getItem('token')
      return !!token
    } else {
      return false
    }
  }

  isAdmin(): boolean{
    if (typeof localStorage !== 'undefined') {
      const role = localStorage.getItem('role');
      return role == 'ADMIN'
    }
    return false
  }

  isUser(): boolean{
    if (typeof localStorage !== 'undefined') {
      const role = localStorage.getItem('role')
      return role === 'USER'
    }
    return false
  }

}

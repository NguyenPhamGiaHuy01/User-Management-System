import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { Router, RouterLink, RouterOutlet } from '@angular/router';
import { UsersService } from '../users.service';

@Component({
  selector: 'app-nav',
  standalone: true,
  imports: [CommonModule, RouterLink, RouterOutlet],
  templateUrl: './nav.component.html',
  styleUrl: './nav.component.scss'
})
export class NavComponent {


constructor(private userService: UsersService){}

  isAuthenticated:boolean = false
  isAdmin:boolean = false
  isUser:boolean = false

  ngOnInit():void{
    this.isAuthenticated = this.userService.isAuthenticated()
    this.isAdmin = this.userService.isAdmin()
    this.isUser = this.userService.isUser()
  }


  logout() {
    this.userService.logOut();
    this.isAuthenticated=false;
    this.isAdmin= false
    this.isUser=false
  }
}

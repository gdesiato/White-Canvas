import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UserService } from './user.service';
import { User } from './user.model';

@Component({
  selector: 'app-user',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent implements OnInit {
  users: User[] = [];
  selectedUser?: User;

  constructor(private userService: UserService) {}

  ngOnInit(): void {
    this.getAllUsers();
  }

  getAllUsers(): void {
    this.userService.getAllUsers().subscribe(
      (users) => {
        this.users = users;
        console.log('Users loaded:', this.users);
      },
      (error) => {
        console.error('Error loading users:', error);
      }
    );
  }

  selectUser(userId: number): void {
    this.userService.getUserById(userId).subscribe((user) => {
      console.log("Selected User:", user); // Debugging output
      this.selectedUser = user;
    });
  }

  deleteUser(userId: number): void {
    this.userService.deleteUser(userId).subscribe(() => {
      this.getAllUsers(); // Refresh the list after deletion
    });
  }
}



export interface User {
    id: number;
    email: string;
    orders?: Order[];
    cart?: Cart;
  }
  
  export interface Order {
    id: number;
  }
  
  export interface Cart {
    id: number;
  }
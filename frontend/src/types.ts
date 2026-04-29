export interface Product {
  id: number;
  name: string;
}

export interface Order {
  id: number;
  ownerId: number;
  sellProductId: number;
  sellAmount: number;
  buyProductId: number;
  buyAmount: number;
  status: 'NEW' | 'RESOLVED' | 'CANCELED';
  createdAt: string;
}

export interface TokenResponse {
  accessToken: string;
  refreshToken: string | null;
}

export interface UserResponse {
  id: number;
  email: string;
}

export interface ApiError {
  timestamp: string;
  status: number;
  error: string;
  message: string;
  path: string;
  fieldErrors?: Record<string, string>;
}

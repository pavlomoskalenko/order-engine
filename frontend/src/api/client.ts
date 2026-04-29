import type { ApiError, Order, Product, TokenResponse, UserResponse } from '../types';

function getAccessToken(): string | null {
  return localStorage.getItem('accessToken');
}

function getRefreshToken(): string | null {
  return localStorage.getItem('refreshToken');
}

export function setTokens(accessToken: string, refreshToken: string | null) {
  localStorage.setItem('accessToken', accessToken);
  if (refreshToken) localStorage.setItem('refreshToken', refreshToken);
}

export function clearTokens() {
  localStorage.removeItem('accessToken');
  localStorage.removeItem('refreshToken');
}

async function handleResponse<T>(response: Response): Promise<T> {
  if (!response.ok) {
    const error: ApiError = await response.json().catch(() => ({
      status: response.status,
      error: response.statusText,
      message: 'An unexpected error occurred',
      path: '',
      timestamp: new Date().toISOString(),
    }));
    throw error;
  }
  if (response.status === 204) return null as T;
  return response.json() as Promise<T>;
}

async function fetchWithAuth(path: string, options: RequestInit = {}): Promise<Response> {
  const headers: Record<string, string> = {
    'Content-Type': 'application/json',
    ...(options.headers as Record<string, string>),
  };

  const token = getAccessToken();
  if (token) headers['Authorization'] = `Bearer ${token}`;

  let response = await fetch(`/api${path}`, { ...options, headers });

  if (response.status === 401) {
    const refreshToken = getRefreshToken();
    if (refreshToken) {
      try {
        const refreshRes = await fetch('/api/refresh', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ refreshToken }),
        });
        if (refreshRes.ok) {
          const tokens: TokenResponse = await refreshRes.json();
          setTokens(tokens.accessToken, tokens.refreshToken);
          headers['Authorization'] = `Bearer ${tokens.accessToken}`;
          response = await fetch(`/api${path}`, { ...options, headers });
          return response;
        }
      } catch {
        // refresh failed — fall through to clear tokens
      }
    }
    clearTokens();
    window.location.href = '/login';
  }

  return response;
}

export const api = {
  async register(email: string, password: string): Promise<UserResponse> {
    const res = await fetch('/api/register', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ email, password }),
    });
    return handleResponse<UserResponse>(res);
  },

  async login(email: string, password: string): Promise<TokenResponse> {
    const res = await fetch('/api/login', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ email, password }),
    });
    const data = await handleResponse<TokenResponse>(res);
    setTokens(data.accessToken, data.refreshToken);
    return data;
  },

  logout: clearTokens,

  async getProducts(): Promise<Product[]> {
    const res = await fetch('/api/products');
    return handleResponse<Product[]>(res);
  },

  async getOrders(): Promise<Order[]> {
    const res = await fetchWithAuth('/orders');
    return handleResponse<Order[]>(res);
  },

  async placeOrder(order: {
    sellProductId: number;
    sellAmount: number;
    buyProductId: number;
    buyAmount: number;
  }): Promise<Order> {
    const res = await fetchWithAuth('/orders', {
      method: 'POST',
      body: JSON.stringify(order),
    });
    return handleResponse<Order>(res);
  },

  async cancelOrder(id: number): Promise<Order> {
    const res = await fetchWithAuth(`/orders/${id}`, { method: 'PATCH' });
    return handleResponse<Order>(res);
  },
};

import { createContext, useContext, useState, type ReactNode } from 'react';
import { clearTokens } from '../api/client';

interface AuthContextValue {
  isAuthenticated: boolean;
  userEmail: string | null;
  login: (email: string) => void;
  logout: () => void;
}

const AuthContext = createContext<AuthContextValue | null>(null);

function parseEmailFromToken(token: string): string | null {
  try {
    const payload = JSON.parse(atob(token.split('.')[1]));
    return (payload.sub as string) ?? null;
  } catch {
    return null;
  }
}

export function AuthProvider({ children }: { children: ReactNode }) {
  const [userEmail, setUserEmail] = useState<string | null>(() => {
    const token = localStorage.getItem('accessToken');
    return token ? parseEmailFromToken(token) : null;
  });

  function login(email: string) {
    setUserEmail(email);
  }

  function logout() {
    clearTokens();
    setUserEmail(null);
  }

  return (
    <AuthContext.Provider value={{ isAuthenticated: userEmail !== null, userEmail, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error('useAuth must be used inside AuthProvider');
  return ctx;
}

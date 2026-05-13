import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      '/api/login':       { target: 'http://localhost:8080', changeOrigin: true },
      '/api/register':    { target: 'http://localhost:8080', changeOrigin: true },
      '/api/refresh':     { target: 'http://localhost:8080', changeOrigin: true },
      '/api/.well-known': { target: 'http://localhost:8080', changeOrigin: true },
      '/api/orders':      { target: 'http://localhost:8081', changeOrigin: true },
      '/api/products':    { target: 'http://localhost:8081', changeOrigin: true },
    },
  },
})

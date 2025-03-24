// src/api/core/auth-server.ts
import { createAxiosInstance } from './base'

const authServer = createAxiosInstance('http://localhost:9000')

export default authServer

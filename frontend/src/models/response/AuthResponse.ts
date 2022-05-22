export interface AuthResponse {
    accessToken: string
    refreshToken: string
    id: number
    email: string
    roles: [string]
}
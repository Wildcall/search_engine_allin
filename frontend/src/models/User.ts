import {UserRole} from "@/models/UserRole";

export interface User {
    id: number
    email: string
    roles: [UserRole]
}
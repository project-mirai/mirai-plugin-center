export interface AdminView{
    adminView?: boolean
}
export function isAdminView(adminView:AdminView):boolean {
    return (adminView.adminView !== undefined && adminView.adminView)
}
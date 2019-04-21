import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IFamilyMember } from 'app/shared/model/family-member.model';

type EntityResponseType = HttpResponse<IFamilyMember>;
type EntityArrayResponseType = HttpResponse<IFamilyMember[]>;

@Injectable({ providedIn: 'root' })
export class FamilyMemberService {
    public resourceUrl = SERVER_API_URL + 'api/family-members';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/family-members';

    constructor(protected http: HttpClient) {}

    create(familyMember: IFamilyMember): Observable<EntityResponseType> {
        return this.http.post<IFamilyMember>(this.resourceUrl, familyMember, { observe: 'response' });
    }

    update(familyMember: IFamilyMember): Observable<EntityResponseType> {
        return this.http.put<IFamilyMember>(this.resourceUrl, familyMember, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IFamilyMember>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IFamilyMember[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IFamilyMember[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}

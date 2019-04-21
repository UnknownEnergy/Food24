import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IFamilyGroup } from 'app/shared/model/family-group.model';

type EntityResponseType = HttpResponse<IFamilyGroup>;
type EntityArrayResponseType = HttpResponse<IFamilyGroup[]>;

@Injectable({ providedIn: 'root' })
export class FamilyGroupService {
    public resourceUrl = SERVER_API_URL + 'api/family-groups';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/family-groups';

    constructor(protected http: HttpClient) {}

    create(familyGroup: IFamilyGroup): Observable<EntityResponseType> {
        return this.http.post<IFamilyGroup>(this.resourceUrl, familyGroup, { observe: 'response' });
    }

    update(familyGroup: IFamilyGroup): Observable<EntityResponseType> {
        return this.http.put<IFamilyGroup>(this.resourceUrl, familyGroup, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IFamilyGroup>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IFamilyGroup[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IFamilyGroup[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}

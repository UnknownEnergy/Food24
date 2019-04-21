import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IStoreItemInstance } from 'app/shared/model/store-item-instance.model';

type EntityResponseType = HttpResponse<IStoreItemInstance>;
type EntityArrayResponseType = HttpResponse<IStoreItemInstance[]>;

@Injectable({ providedIn: 'root' })
export class StoreItemInstanceService {
    public resourceUrl = SERVER_API_URL + 'api/store-item-instances';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/store-item-instances';

    constructor(protected http: HttpClient) {}

    create(storeItemInstance: IStoreItemInstance): Observable<EntityResponseType> {
        return this.http.post<IStoreItemInstance>(this.resourceUrl, storeItemInstance, { observe: 'response' });
    }

    update(storeItemInstance: IStoreItemInstance): Observable<EntityResponseType> {
        return this.http.put<IStoreItemInstance>(this.resourceUrl, storeItemInstance, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IStoreItemInstance>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IStoreItemInstance[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IStoreItemInstance[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}

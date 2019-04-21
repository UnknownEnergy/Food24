import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IStoreItem } from 'app/shared/model/store-item.model';

type EntityResponseType = HttpResponse<IStoreItem>;
type EntityArrayResponseType = HttpResponse<IStoreItem[]>;

@Injectable({ providedIn: 'root' })
export class StoreItemService {
    public resourceUrl = SERVER_API_URL + 'api/store-items';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/store-items';

    constructor(protected http: HttpClient) {}

    create(storeItem: IStoreItem): Observable<EntityResponseType> {
        return this.http.post<IStoreItem>(this.resourceUrl, storeItem, { observe: 'response' });
    }

    update(storeItem: IStoreItem): Observable<EntityResponseType> {
        return this.http.put<IStoreItem>(this.resourceUrl, storeItem, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IStoreItem>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IStoreItem[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IStoreItem[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}

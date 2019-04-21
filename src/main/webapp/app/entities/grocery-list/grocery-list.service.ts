import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IGroceryList } from 'app/shared/model/grocery-list.model';

type EntityResponseType = HttpResponse<IGroceryList>;
type EntityArrayResponseType = HttpResponse<IGroceryList[]>;

@Injectable({ providedIn: 'root' })
export class GroceryListService {
    public resourceUrl = SERVER_API_URL + 'api/grocery-lists';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/grocery-lists';

    constructor(protected http: HttpClient) {}

    create(groceryList: IGroceryList): Observable<EntityResponseType> {
        return this.http.post<IGroceryList>(this.resourceUrl, groceryList, { observe: 'response' });
    }

    update(groceryList: IGroceryList): Observable<EntityResponseType> {
        return this.http.put<IGroceryList>(this.resourceUrl, groceryList, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IGroceryList>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IGroceryList[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IGroceryList[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}

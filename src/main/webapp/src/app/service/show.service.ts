import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {Observable} from "rxjs/index";
import {ApiResponse} from "../model/api.response";

@Injectable()
export class ShowService {

  baseUrl: string = 'http://localhost:8080/shows/all';
  baseQueryUrl: string = 'http://localhost:8080/shows/q';

  constructor(private http: HttpClient) { }

  getShows() : Observable<ApiResponse> {
    return this.http.get<ApiResponse>(this.baseUrl);
  }

  getShowsByFilters(query: String): Observable<ApiResponse> {
    return this.http.get<ApiResponse>(this.baseQueryUrl + query);
  }
}
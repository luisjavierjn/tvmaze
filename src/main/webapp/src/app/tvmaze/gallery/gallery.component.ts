import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import {Router} from "@angular/router";
import { ShowService } from '../../service/show.service';
import { Filter } from '../../model/filter.model';

@Component({
    selector: 'app-shows-gallery',
    templateUrl: './gallery.component.html',
    styleUrls: ['./gallery.component.css']
  })
  export class ShowsGalleryComponent implements OnInit {
    @ViewChild('searchBar',{ static: true }) searchBar: ElementRef;
    @ViewChild('orderBy',{ static: true }) orderBy: ElementRef;
  
    payload: any;
    auxname: string;
    auxused: number;
    ordering: string;
    isadmin: boolean;

    categories: Filter[] = [
        { name: 'keywords', checked: 'checked', used: false, info: '' },
        { name: 'language', checked: '', used: false, info: '' },
        { name: 'genres', checked: '', used: false, info: '' },
        { name: 'channel', checked: '', used: false, info: '' },
        { name: 'days', checked: '', used: false, info: '' },
        { name: 'time', checked: '', used: false, info: '' }
    ]
  
    constructor(private router: Router, private showService: ShowService) { 
        this.auxname = 'keywords';
        this.ordering = '';

        if(window.localStorage.getItem('rol')=='admin') {
            this.isadmin = true;
        }
    }
  
    ngOnInit() {
      if(!window.localStorage.getItem('token')) {
        this.router.navigate(['login']);
        return;
      }
      this.showService.getShows().subscribe( response => {
        this.payload = response;
        console.log(this.payload.result);
      }, error => {
        console.log(error);
      });
    }
  
    getUsers(): void {
        this.router.navigate(['list-user']);
    }

    changeOption(e: any) {
        console.log(e.target.value);
        this.categories.forEach(cat => {
            if(cat.name==e.target.value) {
                cat.checked = 'checked';
                this.auxname = cat.name;
            } else {
                cat.checked = '';
            }
        });
    }

    deleteCat(name: String): void {
        let count = 0;
        let countChk = 0;
        this.categories.forEach(cat => {
            if(cat.used) {
                count++;
            }
            if(cat.used && cat.name==name) {
                cat.info = '';
                cat.used = false;
                cat.checked = '';
                count--;
            }
            if(cat.checked=='checked') {
                countChk++;
            }
        });
        if(countChk == 0) {
            this.categories.forEach(cat => {
                if(cat.name==name) {
                    cat.info = '';
                    cat.checked = 'checked';
                    this.auxname = cat.name;
                }
            });
        }
        this.auxused = count;

        this.makeRequest();
    }

    search(value: string): void {
        value = value.trim();
        if(value=="") return;
        let count = 0;
        let ready = false;
        this.categories.forEach(cat => {
            if(cat.checked=='checked') {
                cat.info = value;
                cat.used = true;
                cat.checked = '';
                count++;
                console.log(cat.name + ' ' + cat.info);
            } else if(!cat.used && !ready) {
                cat.info = '';
                cat.checked = 'checked';
                this.auxname = cat.name;
                ready = true;
            } else {
                cat.checked = '';
                if(!ready) {
                    this.auxname = "No More";
                }
            }
        });
        this.auxused = count;
        if(this.auxname == "No More") this.searchBar.nativeElement.value = "";

        this.makeRequest();
    }

    buildQuery(): String {
        let filters = this.ordering;
        let first = true;
        let count = 0;
        this.categories.forEach(cat => {
            if(cat.used) {
                count++;
                if(first) {
                    filters += "?filter=" + cat.name + ':' + cat.info.trim().replace(/ /gi, '-');
                    first = false;
                } else {
                    filters += "&filter=" + cat.name + ':' + cat.info.trim().replace(/ /gi, '-');
                }
            }
        });
        if(count==0) {
            filters += "?filter=none:"
        }
        console.log(filters);
        return filters;
    }

    order(): void {
        console.log(this.orderBy.nativeElement.value);
        switch(this.orderBy.nativeElement.value) {
            case "1":
                this.ordering = '/RATING/ASC';
                break;
            case "2":
                this.ordering = '/RATING/DESC';
                break;
            case "3":
                this.ordering = '/CHANNEL/ASC';
                break;
            case "4":
                this.ordering = '/CHANNEL/DESC';
                break;
            case "5":
                this.ordering = '/GENRE/ASC';
                break;
            case "6":
                this.ordering = '/GENRE/DESC';
                break;
            default:
                this.ordering = '';
                break;
        }

        this.makeRequest();
    }

    makeRequest(): void {
        let query = this.buildQuery();
        if(query=='') {
            this.showService.getShows().subscribe( response => {
                this.payload = response;
                console.log(this.payload.result);
              }, error => {
                console.log(error);
              });
        }
        else {
            this.searchBar.nativeElement.value = "";
            this.showService.getShowsByFilters(this.buildQuery())
            .subscribe( response => {
                this.payload = response;
                console.log(this.payload.result);
            }, error => {
                console.log(error);
            });            
        }        
    }
  }


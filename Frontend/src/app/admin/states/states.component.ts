import { Component, OnInit } from '@angular/core';
import { WordFilesService } from './word-files.service';
import * as FileSaver from 'file-saver';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-states',
  templateUrl: './states.component.html',
  styleUrls: ['./states.component.css']
})
export class StatesComponent implements OnInit {
  searchQuery: string = '';
  salesReports: any[] = [];
  filteredReports: any[] = [];
  selectedMonth: number | undefined;
  selectedYear: number | undefined;

  constructor(private wordFilesService: WordFilesService, private route: ActivatedRoute,private router:Router) { }

  ngOnInit(): void {
    this.route.queryParamMap.subscribe((params) => {
      const sellerId = params.get('id') || '';
      this.fetchSalesReports(sellerId);
    });
  }

  fetchSalesReports(sellerId: string): void {
    let id: number = +sellerId ;
    this.wordFilesService.getSalesReports(id).subscribe(
      (reports: any[]) => {
        this.salesReports = reports.reverse();
        this.filterReports(); 
      },
      (error) => {
        console.error('Error fetching sales reports:', error);
      }
    );
  }
  
  filterReports(): void {
    if (this.selectedMonth === undefined || this.selectedYear === undefined) {
      this.filteredReports = this.salesReports;
      return; 
    }
    this.filteredReports = this.salesReports.filter((report) => {
      const dateFromFileName = this.extractDateFromFileName(report.fileName);
      if (dateFromFileName) {
        const [reportYear, reportMonth] = dateFromFileName.split('-').map(Number);
        return reportYear == this.selectedYear && reportMonth == this.selectedMonth;
      }
      return false;
    });
  }
  
  filterByMonthAndYear(month: number | undefined, year: number | undefined): void {
    this.selectedMonth = month;
    this.selectedYear = year;
    this.filterReports();
  }
  

clearFilters(): void {
  this.selectedMonth = undefined;
  this.selectedYear = undefined;
  this.filterReports(); 
}
  
  extractDateFromFileName(fileName: string): string {
    const datePattern = /(\d{4}-\d{2}-\d{2})/; 
    const match = fileName.match(datePattern);
    if (match && match.length > 1) {
      return match[1];
    } else {
      console.error(`Date not found in the file name: ${fileName}`);
      return '';
    }
  }
  downloadReport(reportDate: string): void {
    const report = this.salesReports.find((report) => {
      const dateFromFileName = report.fileName.match(/(\d{4}-\d{2}-\d{2})/); 
      if (dateFromFileName) {
        return dateFromFileName[1] === reportDate;
      }
      return false;
    });
  
    if (report) {
      const fileName = report.fileName;
      const fileBytes = this.base64ToArrayBuffer(report.fileBytes);
  
      const blob = new Blob([fileBytes], { type: 'application/vnd.openxmlformats-officedocument.wordprocessingml.document' });
      FileSaver.saveAs(blob, fileName);
    } else {
      console.error(`Report for date ${reportDate} not found.`);
    }
  }
  
  

  private base64ToArrayBuffer(base64: string): Uint8Array {
    const binaryString = atob(base64);
    const len = binaryString.length;
    const bytes = new Uint8Array(len);

    for (let i = 0; i < len; i++) {
      bytes[i] = binaryString.charCodeAt(i);
    }

    return bytes;
  }
  applyFilter(): void {
    if (!this.searchQuery) {
      this.filterReports();
    } else {
      this.filteredReports = this.salesReports.filter((report) => {
        return report.fileName.toLowerCase().includes(this.searchQuery.toLowerCase());
      });
    }
  }

  clearSearch(): void {
    this.searchQuery = '';
    this.filterReports();
  }
  
}

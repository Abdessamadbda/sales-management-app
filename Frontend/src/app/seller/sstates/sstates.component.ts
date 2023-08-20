import { Component } from '@angular/core';
import { AuthService } from '../login/auth.service';
import { ActivatedRoute, Router } from '@angular/router';
import { SalesService } from '../declaration/sales.service';
import * as FileSaver from 'file-saver';

@Component({
  selector: 'app-sstates',
  templateUrl: './sstates.component.html',
  styleUrls: ['./sstates.component.css']
})
export class SstatesComponent {
  salesReports: any[] = [];
  sellerId:number=0
  ngOnInit() {
    this.route.queryParamMap.subscribe((params) => {
      const sellerId = params.get('id1') || '';
      this.fetchSalesReports(sellerId);
    });    

  }
  constructor(private authService: AuthService,private router:Router,private salesService:SalesService,private route: ActivatedRoute ){}

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
      const blob = new Blob([fileBytes], { type: 'application/pdf' });
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
  

  fetchSalesReports(sellerId:string): void {
    let id: number = +sellerId ;
    this.salesService.getSalesReports(id).subscribe(
      (reports: any[]) => {
        this.salesReports = reports.reverse();
      },
      (error) => {
        console.error('Error fetching sales reports:', error);
      }
      
    );
  }
}

import { ComponentFixture, TestBed } from '@angular/core/testing';
import * as MyRequestsModule from './my-requests';

const MyRequests = (MyRequestsModule as any).MyRequests
  ?? (MyRequestsModule as any).MyRequestsComponent
  ?? (MyRequestsModule as any).MyRequestsPage
  ?? (MyRequestsModule as any).default;

describe('MyRequests', () => {
  let component: any;
  let fixture: ComponentFixture<any>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MyRequests],
    }).compileComponents();

    fixture = TestBed.createComponent(MyRequests);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { CotizacionComponent } from './cotizacion';
import { ReservaService } from '../../services/reserva.service';

describe('Cotizacion', () => {
  let component: CotizacionComponent;
  let fixture: ComponentFixture<CotizacionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CotizacionComponent],
      imports: [RouterTestingModule, HttpClientTestingModule],
      providers: [ReservaService],
    }).compileComponents();

    fixture = TestBed.createComponent(CotizacionComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

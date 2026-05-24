import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormsModule } from '@angular/forms';
import { InicioComponent } from './inicio';
import { AuthService } from '../../services/auth.service';
import { TravelSearchService } from '../../services/travel-search.service';
import { ReservaService } from '../../services/reserva.service';
import { LocationService } from '../../services/location.service';
import { AirportService } from '../../services/airport.service';
import { PersonaService } from '../../services/persona.service';

describe('Inicio', () => {
  let component: InicioComponent;
  let fixture: ComponentFixture<InicioComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [InicioComponent],
      imports: [RouterTestingModule, HttpClientTestingModule, FormsModule],
      providers: [
        AuthService,
        TravelSearchService,
        ReservaService,
        LocationService,
        AirportService,
        PersonaService,
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(InicioComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { VerificarCorreoComponent } from './verificar-correo';
import { AuthService } from '../../services/auth.service';

describe('VerificarCorreo', () => {
  let component: VerificarCorreoComponent;
  let fixture: ComponentFixture<VerificarCorreoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [VerificarCorreoComponent],
      imports: [RouterTestingModule, HttpClientTestingModule],
      providers: [AuthService],
    }).compileComponents();

    fixture = TestBed.createComponent(VerificarCorreoComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

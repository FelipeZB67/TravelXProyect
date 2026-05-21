import { Reserva } from './reserva.model';

export interface ReservaInternacionalModel extends Reserva {
  paisOrigen:    string;
  paisDestino:   string;
  requiereVisa:  boolean;
}

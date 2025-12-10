public class Astronave implements Runnable {
    private String nome;
    private double punteggio;
    private double velocita;
    private Giudice giudice;
    private double metri;

    public Astronave(String nome) {
        this.nome = nome;
        this.punteggio = 0.0;
        this.velocita = 0.0;
        this.metri = 0.0;
    }

    @Override
    public void run() {
        while (giudice != null && giudice.isGaraInCorso() && metri < giudice.getMetriTotali()) {
            percorri(velocita);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    public double getPunteggio() {
        return punteggio;
    }

    public void setPunteggio(double punteggio) {
        this.punteggio = punteggio;
    }

    public double getVelocita() {
        return velocita;
    }

    public void setVelocita(double velocita) {
        this.velocita = velocita;
    }

    public void percorri(double tratto) {
        metri += tratto;
        if (giudice != null) {
            giudice.aggiornaAvanzamento(tratto);
        }
    }

    public void notificaAnomalia(String evento, double penalita) {
        punteggio -= penalita;
    }

    public void setGiudice(Giudice giudice) {
        this.giudice = giudice;
    }

    public double getMetri() {
        return metri;
    }

    public String getNome() {
        return nome;
    }
}

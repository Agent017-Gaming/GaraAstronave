import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class Giudice implements Runnable {
    private ArrayList<Astronave> astronavi;
    private ArrayList<String> classifica;
    private double metriTotali;
    private double metriAvanzamento;
    private ArrayList<Thread> threads;
    private Random random;
    private boolean garaInCorso;
    private String[] eventi;

    public Giudice(double metriTotali) {
        this.metriTotali = metriTotali;
        this.astronavi = new ArrayList<>();
        this.classifica = new ArrayList<>();
        this.threads = new ArrayList<>();
        this.random = new Random();
        this.garaInCorso = false;
        this.eventi = new String[0];
    }

    @Override
    public void run() {
        int ciclo = 0;
        while (garaInCorso) {
            verificaVincitore();
            ciclo++;
            if (ciclo % 10 == 0 && !astronavi.isEmpty()) {
                int eventoRandom = random.nextInt(6);
                Astronave astronaveRandom = astronavi.get(random.nextInt(astronavi.size()));
                switch (eventoRandom) {
                    case 0:
                        favorisceAstronave(astronaveRandom);
                        System.out.println(astronaveRandom.getNome() + " riceve una spinta!");
                        break;
                    case 1:
                        sfavorisceAstronave(astronaveRandom);
                        astronaveRandom.notificaAnomalia("Turbolenza spaziale", 5.0);
                        System.out.println(astronaveRandom.getNome() + " rallenta per turbolenza!");
                        break;
                    case 2:
                        rallentaPartecipanti(0.5);
                        System.out.println("Campo gravitazionale rallenta tutti!");
                        break;
                    case 3:
                        visualizzaAstronave(astronaveRandom);
                        break;
                }
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    public void verificaVincitore() {
        for (Astronave a : astronavi) {
            if (a.getMetri() >= metriTotali) {
                garaTermina();
                return;
            }
        }
    }

    public void garaAvvio() {
        garaInCorso = true;
    }

    public void garaTermina() {
        garaInCorso = false;
    }

    public void mostraClassifica() {
        System.out.println("--- Classifica ---");
        List<Astronave> sorted = new ArrayList<>(astronavi);
        sorted.sort(Comparator.comparingDouble(Astronave::getMetri).reversed());
        int pos = 1;
        for (Astronave a : sorted) {
            System.out.printf("%d. %s - %.2f metri\n", pos++, a.getNome(), a.getMetri());
        }
    }

    public void favorisceAstronave(Astronave a) {
        a.setVelocita(a.getVelocita() * 1.1);
    }

    public void sfavorisceAstronave(Astronave a) {
        a.setVelocita(a.getVelocita() * 0.9);
    }

    public void ritiraAstronave(Astronave a) {
        astronavi.remove(a);
    }

    public void fermaAstronave(Astronave a) {
        a.setVelocita(0);
    }

    public void rallentaPartecipanti(double valore) {
        for (Astronave a : astronavi) {
            a.setVelocita(a.getVelocita() - valore);
        }
    }

    public void visualizzaAstronave(Astronave a) {
        System.out.println(a.getNome() + " - " + a.getMetri() + " metri");
    }

    public void aggiungiAstronave(Astronave a) {
        if (!astronavi.contains(a)) {
            astronavi.add(a);
            a.setGiudice(this);
        }
    }

    public double getMetriTotali() {
        return metriTotali;
    }

    public double getMetriAvanzamento() {
        return metriAvanzamento;
    }

    public void salvaClassifica() {
        GestoreFile gestore = new GestoreFile("classifica.txt");
        List<Astronave> sorted = new ArrayList<>(astronavi);
        sorted.sort(Comparator.comparingDouble(Astronave::getMetri).reversed());
        classifica.clear();
        int pos = 1;
        for (Astronave a : sorted) {
            String riga = pos++ + ". " + a.getNome() + " - " + String.format("%.2f", a.getMetri()) + " metri - Punteggio: " + String.format("%.2f", a.getPunteggio());
            classifica.add(riga);
            gestore.scrivi(riga);
        }
    }

    public void mostraClassificaPrecedente() {
        GestoreFile gestore = new GestoreFile("classifica.txt");
        String contenuto = gestore.leggi();
        if (contenuto.isEmpty()) {
            System.out.println("Nessuna classifica precedente trovata.");
        } else {
            System.out.println("\n=== CLASSIFICA PRECEDENTE ===");
            System.out.print(contenuto);
        }
    }

    public boolean isGaraInCorso() {
        return garaInCorso;
    }

    public synchronized void aggiornaAvanzamento(double tratto) {
        metriAvanzamento += tratto;
    }
}

import java.util.*;

public class Project {

    static class Edge {
        int tujuan, waktu;
        Edge(int tujuan, int waktu) {
            this.tujuan = tujuan;
            this.waktu = waktu;
        }
    }

    static class DisjointSet {
        int[] parent;
        DisjointSet(int n) {
            parent = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
            }
        }
        int find(int x) {
            if (parent[x] == x) {
                return x;
            }
            return parent[x] = find(parent[x]);
        }
        void union(int x, int y) {
            int px = find(x);
            int py = find(y);
            if (px != py) {
                parent[py] = px;
            }
        }
    }

    static int TOTAL = 10, busAPosisi = 1, busBPosisi = 6;
    static boolean busAPenuh = false, busBPenuh = false;
    static String[] NAMA = {
        "", "HALTE UNS INN", "HALTE FT", "HALTE FEB",
        "HALTE FH", "HALTE PASCA", "HALTE GERBANG UTARA",
        "HALTE FK", "HALTE MIPA", "HALTE FP"
    };
    static ArrayList<Edge>[] graph = new ArrayList[TOTAL];
    static DisjointSet ds = new DisjointSet(TOTAL);

    static boolean jalurA(int a) {
        if (ds.find(a) == ds.find(1)) {
            return true;
        } else {
            return false;
        }
    }

    static int hitungWaktu(int dari, int ke) {
        if (dari == ke) {
            return 0;
        }
        int[] h = {-1};
        dfs(dari, ke, 0, new boolean[TOTAL], h);
        return h[0];
    }

    static void dfs(int cur, int tujuan, int w, boolean[] v, int[] h) {
        if (cur == tujuan) {
            h[0] = w;
            return;
        }
        v[cur] = true;
        for (Edge e : graph[cur]) {
            if (!v[e.tujuan]) {
                dfs(e.tujuan, tujuan, w + e.waktu, v, h);
                if (h[0] != -1) {
                    return;
                }
            }
        }
    }

    static void tampilkanHalte() {
        String statusA = busAPenuh ? "PENUH" : "Aman";
        System.out.println("\n[ JALUR BARAT - BUS A (" + statusA + ") | Posisi: " + NAMA[busAPosisi] + " ]");
        for (int i = 1; i < TOTAL; i++) {
            if (jalurA(i)) {
                System.out.printf(" %d. %s%n", i, NAMA[i]);
            }
        }

        String statusB = busBPenuh ? "PENUH" : "Aman";
        System.out.println("[ JALUR TIMUR - BUS B (" + statusB + ") | Posisi: " + NAMA[busBPosisi] + " ]");
        for (int i = 1; i < TOTAL; i++) {
            if (!jalurA(i)) {
                System.out.printf(" %d. %s%n", i, NAMA[i]);
            }
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < TOTAL; i++) {
            graph[i] = new ArrayList<>();
        }

        int[] ruteA = {1, 2, 3, 4, 5}, waktuA = {3, 4, 4, 5, 10};
        int[] ruteB = {6, 7, 8, 9},    waktuB = {3, 3, 2, 6};

        for (int i = 0; i < ruteA.length; i++) {
            graph[ruteA[i]].add(new Edge(ruteA[(i + 1) % ruteA.length], waktuA[i]));
            ds.union(ruteA[i], ruteA[(i + 1) % ruteA.length]);
        }
        for (int i = 0; i < ruteB.length; i++) {
            graph[ruteB[i]].add(new Edge(ruteB[(i + 1) % ruteB.length], waktuB[i]));
            ds.union(ruteB[i], ruteB[(i + 1) % ruteB.length]);
        }

        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.print("\n=== BUS KAMPUS UNS ===\n1. Supir\n2. Penumpang\n3. Keluar\nPilih opsi (1-3): ");
            if (!sc.hasNextInt()) {
                sc.next();
                System.out.println("Input tidak valid. Masukkan angka 1-3.");
                continue;
            }
            int peran = sc.nextInt();
            if (peran < 1 || peran > 3) {
                System.out.println("Pilihan tidak valid. Masukkan angka 1-3.");
                continue;
            }

            if (peran == 1) {
                System.out.print("Armada (1: Bus A, 2: Bus B): ");
                if (!sc.hasNextInt()) {
                    sc.next();
                    System.out.println("Input tidak valid. Masukkan angka 1 atau 2.");
                    continue;
                }
                int pilihBus = sc.nextInt();
                if (pilihBus != 1 && pilihBus != 2) {
                    System.out.println("Armada tidak valid. Pilih 1 atau 2.");
                    continue;
                }

                System.out.print("Kapasitas (1: Penuh, 0: Aman): ");
                if (!sc.hasNextInt()) {
                    sc.next();
                    System.out.println("Input tidak valid. Masukkan angka 0 atau 1.");
                    continue;
                }
                int inputPenuh = sc.nextInt();
                if (inputPenuh != 0 && inputPenuh != 1) {
                    System.out.println("Kapasitas tidak valid. Pilih 0 atau 1.");
                    continue;
                }
                boolean penuh = inputPenuh == 1;

                tampilkanHalte();
                System.out.print("Masukkan Nomor Halte Sekarang: ");
                if (!sc.hasNextInt()) {
                    sc.next();
                    System.out.println("Input tidak valid. Masukkan angka 1-9.");
                    continue;
                }
                int nomor = sc.nextInt();
                if (nomor < 1 || nomor > 9) {
                    System.out.println("Halte tidak valid.");
                    continue;
                }

                if (pilihBus == 1) {
                    if (!jalurA(nomor)) {
                        System.out.println("Bus A tidak lewat Jalur Timur!");
                        continue;
                    }
                    busAPenuh = penuh;
                    busAPosisi = nomor;
                } else {
                    if (jalurA(nomor)) {
                        System.out.println("Bus B tidak lewat Jalur Barat!");
                        continue;
                    }
                    busBPenuh = penuh;
                    busBPosisi = nomor;
                }
                System.out.println("Data berhasil diperbarui.");

            } else if (peran == 2) {
                tampilkanHalte();
                System.out.print("Nomor Halte Asal: ");
                if (!sc.hasNextInt()) {
                    sc.next();
                    System.out.println("Input tidak valid. Masukkan angka 1-9.");
                    continue;
                }
                int awal = sc.nextInt();

                System.out.print("Nomor Halte Tujuan: ");
                if (!sc.hasNextInt()) {
                    sc.next();
                    System.out.println("Input tidak valid. Masukkan angka 1-9.");
                    continue;
                }
                int tujuan = sc.nextInt();

                if (awal < 1 || awal > 9 || tujuan < 1 || tujuan > 9) {
                    System.out.println("Halte tidak valid.");
                    continue;
                }
                if (awal == tujuan) {
                    System.out.println("Halte asal dan tujuan sama.");
                    continue;
                }
                if (jalurA(awal) != jalurA(tujuan)) {
                    System.out.println("Rute Berbeda! Bus A (1-5) | Bus B (6-9)");
                    continue;
                }

                String namaBus; boolean penuh; int posBus;
                if (jalurA(awal)) {
                    namaBus = "Bus A (Barat)"; penuh = busAPenuh; posBus = busAPosisi;
                } else {
                    namaBus = "Bus B (Timur)"; penuh = busBPenuh; posBus = busBPosisi;
                }

                System.out.println("\n--- INFO PERJALANAN ---\nStatus Bus: " + namaBus + " | Posisi: " + NAMA[posBus]);

                if (penuh) {
                    System.out.println("Bus PENUH! Disarankan jalan kaki ke halte berikutnya");
                } else {
                    int tiba = hitungWaktu(posBus, awal);
                    if (tiba == 0) {
                        System.out.println("Bus sudah di halte Anda!");
                    } else {
                        System.out.println("Estimasi Bus Tiba: " + tiba + " menit.");
                    }
                    System.out.println("Waktu Perjalanan ke Tujuan: " + hitungWaktu(awal, tujuan) + " menit.");
                }

            } else {
                break;
            }
        }
        sc.close();
    }
}
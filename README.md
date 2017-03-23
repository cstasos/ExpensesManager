#ExpensesManager - Android Application

### Εισαγωγή
Ο σκοπός του έργου είναι η βαθύτερη κατανόηση της σχεδίασης και Ανάπτυξης κινητών εφαρμογών και των εννοιών που συζητήθηκαν κατά τη διάρκεια του εξαμήνου, μέσω της ανάπτυξης μιας εφαρμογής για κινητά τηλέφωνα Android η οποία θα μπορεί να χρησιμοποιηθεί για την καταγραφή και παρακολούθηση εξόδων.

### Περιγραφή συστήματος
Το υπό ανάπτυξη σύστημα είναι ένα σύστημα καταγραφής και παρακολούθησης των καθημερινών εξόδων του χρήστη.

Οι κύριες περιπτώσεις χρήσεις για την εργασία αυτή είναι οι ακόλουθες:
- ΠΧ1. Δημιουργία κατηγοριών εξόδων:

> Σ’ αυτή την περίπτωση χρήσης ο χρήστης δημιουργεί διάφορες κατηγορίες εξόδων. 
> Για παράδειγμα μπορεί να δημιουργήσει μία κατηγορία
> «Έξοδα Σουπερμάρκετ», μία άλλη κατηγορία «Έξοδα μετακίνησης» κ.ο.κ. 
> Οι κατηγορίες θα έχουν μία συνοπτική φράση και μία πλήρη περιγραφή που θα είναι προαιρετική. 
> Οι κατηγορίες θα αποθηκεύονται σε μία ΒΔ SQLite.

- ΠΧ2. Καταγραφή εξόδων:

> Σ’ αυτή την περίπτωση ο χρήστης θα κάνει την πλήρη καταγραφή ενός εξόδου. 
> Για να γίνει αυτό το σύστημα θα ανακτά την ημερομηνία/ώρα του κινητού που θα αποτελέσει την ημερομηνία/ώρα κατά την οποία πραγματοποιήθηκε το έξοδο. 
> Ο χρήστης θα δώσει μία περιγραφή του εξόδου προαιρετικά και θα προσδιορίσει υποχρεωτικά την κατηγορία του εξόδου επιλέγοντας σαν κατηγορία μία από αυτές που δημιούργησε στο (1).
> Αν δεν έχει δημιουργηθεί καμία κατηγορία ακόμη, θα πρέπει να δίνεται η δυνατότητα δημιουργίας από αυτήν την οθόνη ούτως ή άλλως. 
> Ο χρήστης θα δίνει υποχρεωτικά την χρηματική αξία του εξόδου σε ευρώ. 
> Επίσης θα γίνεται καταγραφή της τοποθεσίας μέσω των Google Play services location APIs. 
> Ο χρήστης θα καταγράφει την τοποθεσία που έγινε το έξοδο και προαιρετικά θα την ονοματίζει. 
> Αν κάνει ξανά έξοδο στην ίδια τοποθεσία στο μέλλον θα βγαίνει αυτόματα ως σύσταση το αποθηκευμένο όνομα. 
> Όλα τα στοιχεία θα αποθηκεύονται σε ΒΔ SQLite.

- ΠΧ3. Επιθεώρηση εξόδων: 

> Σ’ αυτή την περίπτωση χρήσης ο χρήστης θα επιθεωρεί όλα τα έξοδα σε λίστα. 
> Θα μπορεί να κάνει κλικ σε ένα έξοδο οπότε και θα βλέπει τις λεπτομέρειές του. 
> Αν θέλει θα μπορεί να κάνει διαγραφή και τροποποίηση του εξόδου.
> Η	τροποποίηση του εξόδου μπορεί να συμπεριλαμβάνει:
> * Αλλαγή περιγραφής εξόδου
> * Αλλαγή αξίας εξόδου
> * Αλλαγή κατηγορίας εξόδου

- ΠΧ4. Ανάλυση εξόδων:

> Σε αυτήν την περίπτωση χρήσης ο χρήστης θα δίνει μία περίοδο ανάλυσης με μία ημερομηνία έναρξης και μία ημερομηνία τέλους. 
> Το σύστημα θα παρουσιάζει όλες τις κατηγορίες εξόδων που έγιναν σε αυτή την περίοδο με φθίνουσα σειρά κατά την συνολική αξία.
> Αν για παράδειγμα η αξία των αγορών σουπερμάρκετ είναι 150 ευρώ για την περίοδο που δόθηκε και τα έξοδα μετακινήσεων είναι 220 ευρώ, 
> θα εμφανισθούν πρώτα τα έξοδα μετακινήσεων με την αξία τους και μετά τα έξοδα σουπερμάρκετ με την αξία τους.


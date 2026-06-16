package com.exemplo.fraudedetector.repository;

import com.exemplo.fraudedetector.model.Transacao;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Repository
public class TransacaoRepository {

    // Pega a instancia do Firestore usando as credenciais ja configuradas no FirebaseConfig
    private Firestore getFirestore() {
        return FirestoreClient.getFirestore();
    }

    // Busca a ultima transacao de um usuario (usada pra calcular o score)
    public Transacao buscarUltimaTransacao(String usuarioId) throws ExecutionException, InterruptedException {
        QuerySnapshot snapshot = getFirestore()
            .collection("transacoes")
            .whereEqualTo("usuarioId", usuarioId)
            .orderBy("timestamp", com.google.cloud.firestore.Query.Direction.DESCENDING)
            .limit(1)
            .get()
            .get();

        List<QueryDocumentSnapshot> docs = snapshot.getDocuments();
        if (docs.isEmpty()) {
            return null;
        }

        return docs.get(0).toObject(Transacao.class);
    }

    // Salva uma transacao nova no Firestore
    public void salvar(Transacao transacao) throws ExecutionException, InterruptedException {
        transacao.setTimestamp(new java.util.Date());
        getFirestore().collection("transacoes").add(transacao).get();
    }

    // Lista as ultimas 50 transacoes ordenadas pela mais recente
    public List<Transacao> listarTodas() throws ExecutionException, InterruptedException {
        QuerySnapshot snapshot = getFirestore()
            .collection("transacoes")
            .orderBy("timestamp", com.google.cloud.firestore.Query.Direction.DESCENDING)
            .limit(50)
            .get()
            .get();

        List<Transacao> lista = new ArrayList<>();
        for (QueryDocumentSnapshot doc : snapshot.getDocuments()) {
            Transacao t = doc.toObject(Transacao.class);
            t.setId(doc.getId());
            lista.add(t);
        }
        return lista;
    }
}

package com.exemplo.fraudedetector.repository;

import com.exemplo.fraudedetector.model.Transacao;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Repository
public class TransacaoRepository {

    private Firestore getFirestore() {
        return FirestoreClient.getFirestore();
    }

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

    public void salvar(Transacao transacao) throws ExecutionException, InterruptedException {
        transacao.setTimestamp(new java.util.Date());
        getFirestore().collection("transacoes").add(transacao).get();
    }
}
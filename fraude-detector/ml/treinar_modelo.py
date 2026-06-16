import pandas as pd
from sklearn.ensemble import RandomForestClassifier
import joblib
import firebase_admin
from firebase_admin import credentials, firestore

cred = credentials.Certificate('src/main/resources/firebase-service-account.json')
firebase_admin.initialize_app(cred)
db = firestore.client()

print("Buscando transacoes do Firestore...")
docs = db.collection('transacoes').stream()

registros = []
for doc in docs:
    d = doc.to_dict()
    registros.append({
        'valor': d.get('valor', 0),
        'hora': int(d.get('hora', '00:00').split(':')[0]),
        'score': d.get('score', 0),
        'fraude': 1 if d.get('score', 0) >= 50 else 0
    })

print(f"{len(registros)} transacoes encontradas.")

if len(registros) < 10:
    print("Poucos dados para treinar.")
    print("Faca mais transacoes pelo dashboard primeiro e rode esse script de novo.")
    exit()

df = pd.DataFrame(registros)
X = df[['valor', 'hora', 'score']]
y = df['fraude']

print("Treinando modelo...")
modelo = RandomForestClassifier(n_estimators=10, random_state=42)
modelo.fit(X, y)

joblib.dump(modelo, 'ml/modelo_fraude.pkl')
print("Modelo salvo com sucesso em ml/modelo_fraude.pkl")
print("Agora rode: python ml/servidor_modelo.py")
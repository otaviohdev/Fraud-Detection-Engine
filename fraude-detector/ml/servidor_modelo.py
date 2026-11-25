from flask import Flask, request, jsonify
import joblib
import os

app = Flask(__name__)
modelo = joblib.load('modelo_fraude.pkl')
print("Modelo carregado. Servidor pronto para receber requisicoes.")

@app.route('/prever', methods=['POST'])
def prever():
    dados = request.get_json()
    entrada = [[
        dados['valor'],
        dados['hora'],
        dados['score']
    ]]
    previsao = modelo.predict(entrada)[0]
    return jsonify({
        'fraude_ml': bool(previsao),
        'label': 'FRAUDE' if previsao == 1 else 'NORMAL'
    })

if __name__ == '__main__':
    port = int(os.environ.get('PORT', 5000))
    app.run(host='0.0.0.0', port=port)

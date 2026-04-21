function makePayment() {

    const amount = document.getElementById("amount").value;

    fetch("/pay", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            userId: 1,
            merchantId: 1,
            amount: amount
        })
    })
    .then(res => res.text())
    .then(data => {
        document.getElementById("result").innerText = data;
    });
}
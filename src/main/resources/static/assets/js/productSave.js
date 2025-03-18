    function productSave(button) {
        const productId = button.getAttribute("data-product-id");
        const userId = button.getAttribute("data-user-id");
        const isSaved = button.getAttribute("data-isSaved");

        fetch(`/productSave`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                product_id: productId,
                user_id:userId

            })
        }).then(data => {
              if (data != null) {
                  alert("찜 성공!");

                   // 좋아요가 눌렸으면 버튼 상태 변경
                  if (isSaved) {
                    button.classList.add("saved");
                   } else {
                    button.classList.remove("saved");
                  }

                  // ✅ 강제로 DOM 다시 그리기 (리렌더링)
                              button.style.display = 'none';
                              setTimeout(() => button.style.display = 'inline-block', 10);
              } else {
                  alert("찜 실패!");
              }
          }).catch(error => console.error("Error:", error));
    }
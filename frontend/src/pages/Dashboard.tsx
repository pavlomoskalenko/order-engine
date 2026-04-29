import { useState, useEffect, useCallback, type FormEvent } from 'react'
import { useNavigate } from 'react-router-dom'
import { api } from '../api/client'
import { useAuth } from '../context/AuthContext'
import type { ApiError, Order, Product } from '../types'

function formatDate(iso: string): string {
  return new Date(iso).toLocaleString(undefined, {
    month: 'short', day: 'numeric', hour: '2-digit', minute: '2-digit',
  })
}

function impliedRate(sellAmt: number, buyAmt: number): string {
  if (!sellAmt || !buyAmt) return '—'
  return (sellAmt / buyAmt).toLocaleString(undefined, { maximumFractionDigits: 6 })
}

export default function Dashboard() {
  const { userEmail, logout } = useAuth()
  const navigate = useNavigate()

  const [products, setProducts] = useState<Product[]>([])
  const [orders, setOrders] = useState<Order[]>([])
  const [loadingOrders, setLoadingOrders] = useState(true)

  // Place-order form
  const [sellProductId, setSellProductId] = useState<number>(0)
  const [sellAmount, setSellAmount] = useState('')
  const [buyProductId, setBuyProductId] = useState<number>(0)
  const [buyAmount, setBuyAmount] = useState('')
  const [placing, setPlacing] = useState(false)
  const [orderError, setOrderError] = useState('')

  const productName = useCallback(
    (id: number) => products.find(p => p.id === id)?.name ?? String(id),
    [products],
  )

  const loadOrders = useCallback(async () => {
    try {
      const data = await api.getOrders()
      setOrders(data.sort((a, b) => b.id - a.id))
    } catch {
      // token refresh / redirect handled in client
    }
  }, [])

  useEffect(() => {
    async function init() {
      try {
        const prods = await api.getProducts()
        setProducts(prods)
        if (prods.length >= 2) {
          setSellProductId(prods[0].id)
          setBuyProductId(prods[1].id)
        }
      } catch { /* ignore */ }
      await loadOrders()
      setLoadingOrders(false)
    }
    init()
  }, [loadOrders])

  // Poll for status updates every 10 s
  useEffect(() => {
    const id = setInterval(loadOrders, 10_000)
    return () => clearInterval(id)
  }, [loadOrders])

  function handleLogout() {
    api.logout()
    logout()
    navigate('/login')
  }

  async function handlePlaceOrder(e: FormEvent) {
    e.preventDefault()
    setOrderError('')
    if (sellProductId === buyProductId) {
      setOrderError('Sell and buy products must differ')
      return
    }
    const sAmt = Number(sellAmount)
    const bAmt = Number(buyAmount)
    if (sAmt <= 0 || bAmt <= 0) {
      setOrderError('Amounts must be positive')
      return
    }
    setPlacing(true)
    try {
      const order = await api.placeOrder({
        sellProductId,
        sellAmount: sAmt,
        buyProductId,
        buyAmount: bAmt,
      })
      setOrders(prev => [order, ...prev])
      setSellAmount('')
      setBuyAmount('')
    } catch (err) {
      setOrderError((err as ApiError).message ?? 'Failed to place order')
    } finally {
      setPlacing(false)
    }
  }

  async function handleCancel(orderId: number) {
    if (!confirm('Cancel this order?')) return
    try {
      const updated = await api.cancelOrder(orderId)
      setOrders(prev => prev.map(o => o.id === orderId ? updated : o))
    } catch (err) {
      alert((err as ApiError).message ?? 'Failed to cancel order')
    }
  }

  const sellProd = products.find(p => p.id === sellProductId)
  const buyProd = products.find(p => p.id === buyProductId)
  const rateLabel = sellAmount && buyAmount && sellProd && buyProd
    ? `1 ${buyProd.name} = ${impliedRate(Number(sellAmount), Number(buyAmount))} ${sellProd.name}`
    : null

  return (
    <div className="dashboard">
      <nav className="navbar">
        <span className="navbar-brand">Order Engine</span>
        <div className="navbar-right">
          <span className="navbar-email">{userEmail}</span>
          <button className="btn-ghost" onClick={handleLogout}>Sign out</button>
        </div>
      </nav>

      <div className="dashboard-body">
        {/* ── Place Order ── */}
        <div className="card" style={{ alignSelf: 'start' }}>
          <div className="card-header">
            <h2>Place Order</h2>
          </div>
          <div className="card-body">
            <form onSubmit={handlePlaceOrder} className="order-form">
              {orderError && <div className="error-banner">{orderError}</div>}

              <div className="form-group">
                <label>Sell</label>
                <div className="pair-row">
                  <input
                    type="number"
                    placeholder="Amount"
                    value={sellAmount}
                    onChange={e => setSellAmount(e.target.value)}
                    min="1"
                    step="1"
                    required
                  />
                  <select
                    value={sellProductId}
                    onChange={e => setSellProductId(Number(e.target.value))}
                  >
                    {products.map(p => (
                      <option key={p.id} value={p.id}>{p.name}</option>
                    ))}
                  </select>
                </div>
              </div>

              <div className="form-group">
                <label>Buy</label>
                <div className="pair-row">
                  <input
                    type="number"
                    placeholder="Amount"
                    value={buyAmount}
                    onChange={e => setBuyAmount(e.target.value)}
                    min="1"
                    step="1"
                    required
                  />
                  <select
                    value={buyProductId}
                    onChange={e => setBuyProductId(Number(e.target.value))}
                  >
                    {products.map(p => (
                      <option key={p.id} value={p.id}>{p.name}</option>
                    ))}
                  </select>
                </div>
              </div>

              <div className="rate-box">
                {rateLabel
                  ? <><span>Rate:</span><span className="rate-value">{rateLabel}</span></>
                  : <span>Enter amounts to see rate</span>
                }
              </div>

              <button type="submit" className="btn-primary" disabled={placing || products.length === 0}>
                {placing ? 'Placing…' : 'Place Order'}
              </button>
            </form>
          </div>
        </div>

        {/* ── My Orders ── */}
        <div className="card">
          <div className="card-header">
            <h2>My Orders</h2>
            <button className="refresh-btn" onClick={loadOrders} title="Refresh">↻ Refresh</button>
          </div>

          {loadingOrders ? (
            <div className="empty-state">Loading orders…</div>
          ) : orders.length === 0 ? (
            <div className="empty-state">No orders yet. Place one to get started.</div>
          ) : (
            <div className="orders-scroll">
              <table>
                <thead>
                  <tr>
                    <th>#</th>
                    <th>Sell</th>
                    <th>Buy</th>
                    <th>Rate</th>
                    <th>Status</th>
                    <th>Created</th>
                    <th></th>
                  </tr>
                </thead>
                <tbody>
                  {orders.map(order => (
                    <tr key={order.id}>
                      <td className="id-cell">{order.id}</td>
                      <td className="amount-cell">
                        {order.sellAmount.toLocaleString()} {productName(order.sellProductId)}
                      </td>
                      <td className="amount-cell">
                        {order.buyAmount.toLocaleString()} {productName(order.buyProductId)}
                      </td>
                      <td className="amount-cell" style={{ color: 'var(--text-muted)', fontSize: '0.8125rem' }}>
                        {impliedRate(order.sellAmount, order.buyAmount)}{' '}
                        {productName(order.sellProductId)}/{productName(order.buyProductId)}
                      </td>
                      <td>
                        <span className={`badge badge-${order.status.toLowerCase()}`}>
                          {order.status}
                        </span>
                      </td>
                      <td style={{ color: 'var(--text-muted)', fontSize: '0.8rem', whiteSpace: 'nowrap' }}>
                        {formatDate(order.createdAt)}
                      </td>
                      <td>
                        {order.status === 'NEW' && (
                          <button
                            className="btn-danger-sm"
                            onClick={() => handleCancel(order.id)}
                          >
                            Cancel
                          </button>
                        )}
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </div>
      </div>
    </div>
  )
}
